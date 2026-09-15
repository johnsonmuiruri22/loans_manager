package com.loan_manager_app.loans_manager.SHARED.deviceInfo;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class DeviceInfoExtractor {
    private final HttpServletRequest request;

    public DeviceInfoExtractor(HttpServletRequest request) {
        this.request = request;
    }

    public DeviceInfo extract() {
        String ipAddress = extractIpAddress();
        String userAgent = request.getHeader("User-Agent");

        if (userAgent == null || userAgent.isBlank()) {
            userAgent = "Unknown";
        }

        return DeviceInfo.builder()
                .ipAddress(ipAddress)
                .operatingSystem(extractOperatingSystem(userAgent))
                .deviceName(extractDeviceName(userAgent))
                .deviceType(extractDeviceType(userAgent))
                .build();
    }

    private String extractIpAddress() {
        String forwardedFor = request.getHeader("X-Forwarded-For");
        if (forwardedFor != null && !forwardedFor.isBlank()) {
            // X-Forwarded-For can contain:
            // clientIP, proxy1, proxy2
            return forwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    private String extractOperatingSystem(String userAgent) {
        if (userAgent.contains("Windows")) {
            return "Windows";
        }
        if (userAgent.contains("Android")) {
            return "Android";
        }
        if (userAgent.contains("iPhone")) {
            return "iOS";
        }
        if (userAgent.contains("iPad")) {
            return "iPadOS";
        }
        if (userAgent.contains("Mac OS X")) {
            return "macOS";
        }
        if (userAgent.contains("Linux")) {
            return "Linux";
        }
        return "Unknown";
    }

    private String extractDeviceType(String userAgent) {
        if (userAgent.contains("Mobile")) {
            return "MOBILE";
        }
        if (userAgent.contains("Tablet")
                || userAgent.contains("iPad")) {
            return "TABLET";
        }
        return "DESKTOP";
    }

    private String extractDeviceName(String userAgent) {
        if (userAgent.contains("iPhone")) {
            return "iPhone";
        }
        if (userAgent.contains("iPad")) {
            return "iPad";
        }
        if (userAgent.contains("Android")) {
            return "Android Device";
        }
        if (userAgent.contains("Windows")) {
            return "Windows PC";
        }
        if (userAgent.contains("Macintosh")) {
            return "Mac";
        }
        return "Unknown Device";
    }
}
