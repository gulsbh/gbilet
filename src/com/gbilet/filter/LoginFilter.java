package com.gbilet.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gbilet.entity.User;

public class LoginFilter implements Filter{

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		User user = (User)((HttpServletRequest)request).getSession().getAttribute("user");
		
		String requestPage = ((HttpServletRequest)request).getServletPath();
		String contextPath = ((HttpServletRequest)request).getContextPath();
		System.out.println(requestPage);
		if(user==null){
			((HttpServletResponse)response).sendRedirect(contextPath + "/index.jsf");
			return;
		}
		else if(user.getRole().equals("C") && requestPage.contains("user")){
			((HttpServletResponse)response).sendRedirect(contextPath + "/index.jsf");
			return;
		}
		else if(user.getRole().equals("S") && requestPage.contains("admin")){
			((HttpServletResponse)response).sendRedirect(contextPath + "/index.jsf");
			return;
		}
		else if(user.getRole().equals("O") && requestPage.contains("organizator")){
			((HttpServletResponse)response).sendRedirect(contextPath + "/index.jsf");
			return;
		}
		else if(user.getRole().equals("W") && requestPage.contains("organizator")){
			((HttpServletResponse)response).sendRedirect(contextPath + "/index.jsf");
			return;
		}
		
		chain.doFilter(request, response);
		return;
		
		
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}
	
}
