package ies.sotero.cstore.service;

import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import ies.sotero.cstore.model.CustomUser;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

	@Autowired
	private IUserService userService;

	@Autowired
	private BCryptPasswordEncoder bCrypt;

	@Autowired
	HttpSession session;

	private final Logger LOGGER = LoggerFactory.getLogger(UserDetailServiceImpl.class);

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		LOGGER.info("Username: {}", username);

		Optional<CustomUser> optionalUser = userService.finByEmail(username);
		if (optionalUser.isPresent()) {
			LOGGER.info("User ID: {}", optionalUser.get().getId());

			session.setAttribute("userId", optionalUser.get().getId());

			CustomUser user = optionalUser.get();

			return User.builder().username(user.getName()).password(user.getPassword()).roles(user.getType()).build();

//			return User.builder().username(user.getName()).password(bCrypt.encode(user.getPassword()))
//					.roles(user.getType()).build();
		} else {
			throw new UsernameNotFoundException("User not found");
		}
	}

}
