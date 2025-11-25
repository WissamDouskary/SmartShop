package com.smartshop.shop.utils;

import com.smartshop.shop.enums.Role;
import jakarta.servlet.http.HttpServletRequest;

public class AdminChecker {
    public static boolean isAdmin(HttpServletRequest request){
        Role userRole = (Role) request.getSession().getAttribute("USER_ROLE");
        return userRole.equals(Role.ADMIN);
    }
}
