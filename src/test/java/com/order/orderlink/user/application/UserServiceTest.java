package com.order.orderlink.user.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import com.order.orderlink.user.exception.UserException;
import com.order.orderlink.user.application.dtos.UserRequest;
import com.order.orderlink.user.application.dtos.UserResponse;
import com.order.orderlink.user.domain.User;
import com.order.orderlink.user.domain.UserRoleEnum;
import com.order.orderlink.user.domain.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@InjectMocks
	private UserService userService;

	private User dummyUser;
	private UUID dummyUserId;

	@BeforeEach
	public void setUp() {
		dummyUserId = UUID.randomUUID();
		dummyUser = new User("testuser", "Test Nick", "test@example.com", "01012345678", "encodedPassword",
			UserRoleEnum.CUSTOMER);
		ReflectionTestUtils.setField(dummyUser, "id", dummyUserId);
		ReflectionTestUtils.setField(dummyUser, "createdAt", LocalDateTime.now());
	}

	@Test
	@DisplayName("회원가입 성공")
	public void testSignup_Success() {
		// Given
		UserRequest.Create request = new UserRequest.Create();
		request.setUsername("testuser");
		request.setNickname("Test Nick");
		request.setEmail("test@example.com");
		request.setPhone("01012345678");
		request.setPassword("password");
		request.setRole("CUSTOMER");

		when(userRepository.existsByUsername("testuser")).thenReturn(false);
		when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
		when(userRepository.existsByNickname("Test Nick")).thenReturn(false);
		when(userRepository.existsByPhone("01012345678")).thenReturn(false);
		when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
		when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
			User savedUser = invocation.getArgument(0);
			ReflectionTestUtils.setField(savedUser, "id", dummyUserId);
			return savedUser;
		});

		// When
		UserResponse.Create response = userService.signup(request);

		// Then
		assertNotNull(response);
		assertEquals(dummyUserId, response.getId());
	}

	@Test
	@DisplayName("회원가입 실패: 중복된 아이디")
	public void testSignup_Duplicate() {
		// Given
		UserRequest.Create request = new UserRequest.Create();
		request.setUsername("testuser");
		request.setNickname("Test Nick");
		request.setEmail("test@example.com");
		request.setPhone("01012345678");
		request.setPassword("password");
		request.setRole("USER");

		when(userRepository.existsByUsername("testuser")).thenReturn(true);

		// When / Then
		assertThrows(UserException.class, () -> userService.signup(request));
	}

	@Test
	@DisplayName("회원 목록 조회")
	public void testGetAllUsers() {
		// Given
		Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());
		Page<User> userPage = new PageImpl<>(Collections.singletonList(dummyUser), pageable, 1);
		when(userRepository.findAll(pageable)).thenReturn(userPage);

		// When
		UserResponse.ReadUserList response = userService.getAllUsers(pageable);

		// Then
		assertNotNull(response);
		assertEquals(1, response.getTotalElements());
		assertEquals(1, response.getUsers().size());
		assertEquals(dummyUser.getUsername(), response.getUsers().get(0).getUsername());
	}

	@Test
	@DisplayName("회원 상세 조회: 성공")
	public void testGetMyInfo_Success() {
		// Given
		when(userRepository.findById(dummyUserId)).thenReturn(Optional.of(dummyUser));

		// When
		UserResponse.Read response = userService.getMyInfo(dummyUserId);

		// Then
		assertNotNull(response);
		assertEquals(dummyUser.getUsername(), response.getUsername());
	}

	@Test
	@DisplayName("회원 상세 조회: 실패 - 회원 없음")
	public void testGetUserInfoByAdmin_UserNotFound() {
		// Given
		when(userRepository.findById(dummyUserId)).thenReturn(Optional.empty());

		// When/Then
		assertThrows(UserException.class, () -> userService.getUserInfoByAdmin(dummyUserId));
	}

	@Test
	public void testUpdateInfo() {
		// Given
		UserRequest.Update request = new UserRequest.Update();
		request.setEmail("new@example.com");
		request.setPhone("01098765432");
		request.setNickname("New Nick");
		request.setIsPublic(false);

		when(userRepository.findById(dummyUserId)).thenReturn(Optional.of(dummyUser));

		// When
		UserResponse.Update response = userService.updateInfo(dummyUserId, request);

		// Then
		assertNotNull(response);
		assertEquals("new@example.com", response.getEmail());
		assertEquals("01098765432", response.getPhone());
		assertEquals("New Nick", response.getNickname());
		assertFalse(response.getIsPublic());
	}

	@Test
	@DisplayName("회원 정보 수정 (관리자): 성공")
	public void testUpdateInfoByAdmin_WithRole() {
		// Given
		UserRequest.UpdateByAdmin request = new UserRequest.UpdateByAdmin();
		request.setEmail("adminupdate@example.com");
		request.setPhone("01011112222");
		request.setNickname("Admin Nick");
		request.setIsPublic(true);
		request.setRole("OWNER");

		when(userRepository.findById(dummyUserId)).thenReturn(Optional.of(dummyUser));

		// When
		UserResponse.UpdateByAdmin response = userService.updateInfoByAdmin(dummyUserId, request);

		// Then
		assertNotNull(response);
		assertEquals("adminupdate@example.com", response.getEmail());
		assertEquals("01011112222", response.getPhone());
		assertEquals("Admin Nick", response.getNickname());
		assertTrue(response.getIsPublic());
		assertEquals("OWNER", response.getRole());
	}

	@Test
	@DisplayName("비밀번호 변경 성공")
	public void testUpdatePassword_Success() {
		// Given
		UserRequest.UpdatePassword request = new UserRequest.UpdatePassword();
		request.setCurrentPassword("currentPass");
		request.setNewPassword("newPass");
		request.setNewPasswordConfirm("newPass");

		when(userRepository.findById(dummyUserId)).thenReturn(Optional.of(dummyUser));
		when(passwordEncoder.matches("currentPass", dummyUser.getPassword())).thenReturn(true);
		when(passwordEncoder.encode("newPass")).thenReturn("newEncodedPass");

		// When
		UserResponse.UpdatePassword response = userService.updatePassword(dummyUserId, request);

		// Then
		assertNotNull(response);
		assertEquals("newEncodedPass", dummyUser.getPassword());
	}

	@Test
	@DisplayName("비밀번호 변경 실패: 현재 비밀번호 불일치")
	public void testUpdatePassword_CurrentPasswordMismatch() {
		// Given
		UserRequest.UpdatePassword request = new UserRequest.UpdatePassword();
		request.setCurrentPassword("wrongPass");
		request.setNewPassword("newPass");
		request.setNewPasswordConfirm("newPass");

		when(userRepository.findById(dummyUserId)).thenReturn(Optional.of(dummyUser));
		when(passwordEncoder.matches("wrongPass", dummyUser.getPassword())).thenReturn(false);

		// When/Then
		assertThrows(UserException.class, () -> userService.updatePassword(dummyUserId, request));
	}

	@Test
	@DisplayName("비밀번호 변경 실패: 새 비밀번호와 확인 비밀번호 불일치")
	public void testUpdatePassword_ConfirmationMismatch() {
		// Given
		UserRequest.UpdatePassword request = new UserRequest.UpdatePassword();
		request.setCurrentPassword("currentPass");
		request.setNewPassword("newPass");
		request.setNewPasswordConfirm("differentPass");

		when(userRepository.findById(dummyUserId)).thenReturn(Optional.of(dummyUser));
		when(passwordEncoder.matches("currentPass", dummyUser.getPassword())).thenReturn(true);

		// When/Then
		assertThrows(UserException.class, () -> userService.updatePassword(dummyUserId, request));
	}

	@Test
	@DisplayName("회원 탈퇴 성공")
	public void testSoftDeleteUser_Success() {
		// Given
		UserRequest.Delete deleteRequest = new UserRequest.Delete();
		deleteRequest.setPassword("deletePass");

		when(userRepository.findById(dummyUserId)).thenReturn(Optional.of(dummyUser));
		when(passwordEncoder.matches("deletePass", dummyUser.getPassword())).thenReturn(true);

		// When
		UserResponse.Delete response = userService.softDeleteUser(dummyUserId, dummyUser.getUsername(), "deletePass");

		// Then
		assertNotNull(response);
		assertEquals(dummyUserId, response.getId());
		// softDelete 후 deletedAt 필드가 업데이트되었는지 확인
		assertNotNull(dummyUser.getDeletedAt());
	}

	@Test
	@DisplayName("회원 탈퇴 실패: 비밀번호 불일치")
	public void testSoftDeleteUser_PasswordMismatch() {
		// Given
		UserRequest.Delete deleteRequest = new UserRequest.Delete();
		deleteRequest.setPassword("wrongPass");

		when(userRepository.findById(dummyUserId)).thenReturn(Optional.of(dummyUser));
		when(passwordEncoder.matches("wrongPass", dummyUser.getPassword())).thenReturn(false);

		// When/Then
		assertThrows(UserException.class,
			() -> userService.softDeleteUser(dummyUserId, dummyUser.getUsername(), "wrongPass"));
	}
}