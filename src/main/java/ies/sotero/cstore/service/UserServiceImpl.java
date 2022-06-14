package ies.sotero.cstore.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ies.sotero.cstore.model.CustomUser;
import ies.sotero.cstore.repository.IUserRepository;

@Service
public class UserServiceImpl implements IUserService {

	@Autowired
	private IUserRepository userRepository;

	@Override
	public Optional<CustomUser> findbyId(Integer id) {
		return userRepository.findById(id);
	}

	@Override
	public CustomUser save(CustomUser user) {
		return userRepository.save(user);
	}

	@Override
	public Optional<CustomUser> finByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public List<CustomUser> findAll() {
		return userRepository.findAll();
	}

}
