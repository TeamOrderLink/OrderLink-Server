package com.order.orderlink.common.external.s3;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class S3Service {
	private final AmazonS3 amazonS3;

	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	private static final int EXPIRED_TIME = 3; // 3분

	/**
	 * S3에 이미지 업로드 후 URL 반환
	 */
	public String uploadFile(MultipartFile file) {
		String fileName = generateFileName(file.getOriginalFilename());

		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(file.getSize());
		metadata.setContentType(file.getContentType());

		try (InputStream inputStream = file.getInputStream()) {
			amazonS3.putObject(new PutObjectRequest(bucket, fileName, inputStream, metadata));
			return amazonS3.getUrl(bucket, fileName).toString();
		} catch (IOException e) {
			throw new RuntimeException("파일 업로드 중 오류 발생", e);
		}
	}

	/**
	 * S3에서 파일 삭제
	 */
	public void deleteFile(String fileUrl) {
		String fileKey = extractFileKey(fileUrl);
		amazonS3.deleteObject(new DeleteObjectRequest(bucket, fileKey));
	}

	/**
	 * S3에서 파일 다운로드를 위한 Pre-Signed URL 생성
	 */
	public String getPreSignedUrl(String fileUrl) {
		String fileKey = extractFileKey(fileUrl);

		GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucket, fileKey)
			.withMethod(HttpMethod.GET)
			.withExpiration(getExpirationTime());

		URL preSignedUrl = amazonS3.generatePresignedUrl(request);
		return preSignedUrl.toString();
	}

	/**
	 * 파일 이름 생성 (UUID 적용)
	 */
	private String generateFileName(String originalFilename) {
		return UUID.randomUUID() + getFileExtension(originalFilename);
	}

	/**
	 * 파일 확장자 검증 및 추출
	 */
	private String getFileExtension(String fileName) {
		if (fileName == null || fileName.isEmpty()) {
			throw new IllegalArgumentException("파일명이 비어 있습니다.");
		}

		List<String> allowedExtensions = List.of(".jpg", ".jpeg", ".png", ".heic", ".JPG", ".JPEG", ".PNG", ".HEIC");
		String extension = fileName.substring(fileName.lastIndexOf("."));

		if (!allowedExtensions.contains(extension)) {
			throw new IllegalArgumentException("허용되지 않은 파일 확장자입니다: " + extension);
		}

		return extension;
	}

	/**
	 * URL에서 S3 파일 키 추출
	 */
	private String extractFileKey(String fileUrl) {
		String fileKey = fileUrl.replace(amazonS3.getUrl(bucket, "").toString(), "");
		return fileKey.startsWith("/") ? fileKey.substring(1) : fileKey;
	}

	/**
	 * Pre-Signed URL 만료 시간 설정
	 */
	private Date getExpirationTime() {
		Date expiration = new Date();
		expiration.setTime(expiration.getTime() + TimeUnit.MINUTES.toMillis(EXPIRED_TIME));
		return expiration;
	}
}
