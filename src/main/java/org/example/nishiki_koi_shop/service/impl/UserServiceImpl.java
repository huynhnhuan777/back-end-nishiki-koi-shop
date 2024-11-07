package org.example.nishiki_koi_shop.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.nishiki_koi_shop.model.dto.UserDto;
import org.example.nishiki_koi_shop.model.entity.Role;
import org.example.nishiki_koi_shop.model.entity.User;
import org.example.nishiki_koi_shop.model.payload.ChangePasswordForm;
import org.example.nishiki_koi_shop.model.payload.UserForm;
import org.example.nishiki_koi_shop.repository.RoleRepository;
import org.example.nishiki_koi_shop.repository.UserRepository;
import org.example.nishiki_koi_shop.service.UserService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public void softDeleteUser(Long id, Principal principal) {
        // Lấy tên người dùng từ Principal
        String currentUserEmail = principal.getName();

        // Tìm người dùng hiện tại trong cơ sở dữ liệu dựa trên email
        User currentUser = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("Current user not found"));

        // Lấy ID của người dùng đang đăng nhập
        long currentUserId = currentUser.getId();

        // Tìm người dùng cần xóa
        User userToDelete = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));

        // Kiểm tra nếu người dùng đang đăng nhập
        if (id.equals(currentUserId)) {
            throw new RuntimeException("Cannot delete the currently logged-in user.");
        }

        // Kiểm tra nếu người dùng đã bị xóa mềm trước đó
        if (userToDelete.getDeletedAt() != null) {
            throw new RuntimeException("User was already deleted on: " + userToDelete.getDeletedAt());
        }

        // Thực hiện xóa mềm người dùng
        userToDelete.setDeletedAt(LocalDate.now());
        userRepository.save(userToDelete);
        log.info("User with ID {} has been soft deleted by user with ID {}", id, currentUserId);
    }

    @Override
    public void restoreUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getDeletedAt() == null) {
            throw new RuntimeException("User is not deleted");
        }

        user.setDeletedAt(null);
        userRepository.save(user);
        log.info("User with ID {} restored", userId);
    }

    @Override
    public UserDto getUserById(long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (user.getDeletedAt() != null) {
            throw new RuntimeException("User has been deleted");
        }

        return UserDto.from(user);
    }

    @Override
    public UserDto getMyInfo() {
        var context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();

        log.info("Authentication object: {}", authentication);

        if (authentication == null || !authentication.isAuthenticated()) {
            log.warn("User not authenticated");
            throw new RuntimeException("User not authenticated");
        }

        String name = authentication.getName();
        log.info("Authenticated user name: {}", name);

        User user = userRepository.findByEmail(name).orElseThrow(
                () -> new RuntimeException("User not found")
        );

        return UserDto.from(user);
    }


    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .filter(user -> user.getDeletedAt() == null) // Lọc người dùng chưa bị xóa
                .map(UserDto::from)
                .collect(Collectors.toList());
    }
//
    @Override
    public UserDto updateUser(Long id, UserForm form) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (user.getDeletedAt() != null) {
            throw new RuntimeException("User has been deleted");
        }
        Role role = roleRepository.findByName(form.getRoleName()).orElseThrow(() -> new RuntimeException("Role not found"));

        user.setFullName(form.getFullName());
        user.setAddress(form.getAddress());
        user.setPhoneNumber(form.getPhoneNumber());
        user.setUsername(form.getUsername());
        user.setRole(role);

        userRepository.save(user);
        log.info("User with ID {} updated successfully", id);
        return UserDto.from(user);
    }
    @Override
    public UserDto updateMyInfo(long id, Principal principal, UserForm form) {
        // Lấy thông tin người dùng đang đăng nhập từ Principal
        String loggedInUserEmail = principal.getName();
        User loggedInUser = userRepository.findByEmail(loggedInUserEmail)
                .orElseThrow(() -> new RuntimeException("Logged in user not found"));

        // Kiểm tra xem người dùng đang cố gắng cập nhật thông tin của chính họ
        if (loggedInUser.getId() != id) {
            throw new AccessDeniedException("You do not have permission to update this user.");
        }

        // Tìm kiếm người dùng theo ID
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Cập nhật thông tin người dùng từ form
        user.setFullName(form.getFullName());
        user.setPhoneNumber(form.getPhoneNumber());
        user.setAddress(form.getAddress());
        user.setUsername(form.getUsername());

        // Lưu lại thay đổi vào cơ sở dữ liệu
        userRepository.save(user);

        // Chuyển đổi và trả về thông tin người dùng đã cập nhật
        return UserDto.from(user);
    }
    @Override
    public String changePassword(ChangePasswordForm request, Principal connectedUser) {
        User user = userRepository.findByEmail(connectedUser.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (user.getDeletedAt() != null) {
            return "Cannot change password for a deleted user";
        }

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            return "Current password is incorrect";
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            return "New password and confirm password do not match";
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        log.info("Password changed successfully for user with email {}", user.getEmail());
        return "Password changed successfully";
    }


}
