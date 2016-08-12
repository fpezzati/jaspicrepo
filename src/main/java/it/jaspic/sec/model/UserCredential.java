package it.jaspic.sec.model;

import java.io.Serializable;
import java.security.Principal;
import java.security.acl.Group;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("serial")
public class UserCredential implements Group, Serializable {

	private Set<Principal> principals = new HashSet<Principal>();

	@Override
	public String getName() {
		return "Roles";
	}

	@Override
	public boolean addMember(Principal user) {
		if (!principals.contains(user)) {
			principals.add(user);
			return true;
		}
		return false;
	}

	@Override
	public boolean isMember(Principal member) {
		return principals.contains(member);
	}

	@Override
	public Enumeration<? extends Principal> members() {
		return Collections.enumeration(principals);
	}

	@Override
	public boolean removeMember(Principal user) {
		if (!principals.contains(user)) {
			principals.remove(user);
			return true;
		}
		return false;
	}
}
