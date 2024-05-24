package com.belleange.mall.dto;

import lombok.Getter;
import lombok.Setter;

@Getter  // Lombok 어노테이션으로, 모든 필드에 대한 getter 메서드를 자동으로 생성합니다.
@Setter  // Lombok 어노테이션으로, 모든 필드에 대한 setter 메서드를 자동으로 생성합니다.
public class ApiResponse<T> {  // 제네릭 타입 T를 사용하여 다양한 타입의 응답 데이터를 처리할 수 있도록 합니다.
    private boolean success;  // API 요청이 성공했는지 여부를 나타내는 필드입니다.
    private T data;  // API 응답 데이터가 저장되는 필드입니다.
    private String error;  // 오류 메시지를 저장하는 필드입니다.

    // 성공적인 API 응답을 생성하는 정적 메서드입니다.
    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> response = new ApiResponse<>();  // 새로운 ApiResponse 객체를 생성합니다.
        response.setSuccess(true);  // 요청이 성공했음을 나타냅니다.
        response.setData(data);  // 응답 데이터를 설정합니다.
        return response;  // ApiResponse 객체를 반환합니다.
    }

    // 성공적인 API 응답을 생성하는 정적 메서드로, 데이터가 없는 경우에 사용됩니다.
    public static <T> ApiResponse<T> success() {
        ApiResponse<T> response = new ApiResponse<>();  // 새로운 ApiResponse 객체를 생성합니다.
        response.setSuccess(true);  // 요청이 성공했음을 나타냅니다.
        return response;  // ApiResponse 객체를 반환합니다.
    }

    // 실패한 API 응답을 생성하는 정적 메서드입니다.
    public static <T> ApiResponse<T> error(String error) {
        ApiResponse<T> response = new ApiResponse<>();  // 새로운 ApiResponse 객체를 생성합니다.
        response.setSuccess(false);  // 요청이 실패했음을 나타냅니다.
        response.setError(error);  // 오류 메시지를 설정합니다.
        return response;  // ApiResponse 객체를 반환합니다.
    }
}
