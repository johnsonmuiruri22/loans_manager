package com.loan_manager_app.loans_manager.AUTH.service;

import com.loan_manager_app.loans_manager.SHARED.GlobalEvents.ClientInfo;
import jakarta.servlet.http.HttpServletRequest;

public interface ClientInfoService {
    ClientInfo extractClientInfo(HttpServletRequest request);
}
