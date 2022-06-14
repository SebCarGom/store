package ies.sotero.cstore.service;

import java.util.List;
import java.util.Optional;

import ies.sotero.cstore.model.CustomUser;

public interface IUserService {
	public Optional<CustomUser> findbyId(Integer id);

	public CustomUser save(CustomUser user);

	public Optional<CustomUser> finByEmail(String email);

	public List<CustomUser> findAll();
}
