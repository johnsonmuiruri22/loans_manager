package com.loan_manager_app.loans_manager.AUTH.servImpl;


import com.loan_manager_app.loans_manager.AUTH.service.ClientInfoService;
import com.loan_manager_app.loans_manager.SHARED.GlobalEvents.ClientInfo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public class ClientInfoServiceImpl implements ClientInfoService {

    @Override
    public ClientInfo extractClientInfo(HttpServletRequest request) {
        String ip = getClientIp(request);
        String userAgent = request.getHeader("User-Agent");
        String device = userAgent == null ? "Unknown Device" : userAgent;
        return new ClientInfo(ip, userAgent,device);
    }

    private String getClientIp(
            HttpServletRequest request
    ) {

        String forwarded =
                request.getHeader("X-Forwarded-For");

        if (forwarded != null && !forwarded.isBlank()) {

            return forwarded.split(",")[0].trim();

        }
        return request.getRemoteAddr();
    }
}
