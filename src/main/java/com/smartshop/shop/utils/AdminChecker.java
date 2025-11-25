package com.smartshop.shop.utils;

import com.smartshop.shop.enums.Role;
import com.smartshop.shop.exception.AccessDeniedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class AdminChecker {
    public static boolean isAdmin(HttpServletRequest request){
        Role userRole = (Role) request.getSession().getAttribute("USER_ROLE");
        return userRole.equals(Role.ADMIN);
    }

    public static void checkAdminAccess(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new AccessDeniedException("You must be logged in.");
        }
        Role role = (Role) session.getAttribute("USER_ROLE");
        if (role != Role.ADMIN) {
            throw new AccessDeniedException("Access Denied: Only Admins can manage products.");
        }
    }
}
