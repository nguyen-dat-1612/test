package ptithcm.DAO;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import ptithcm.entity.Account;
import ptithcm.entity.Role;

@Transactional
public interface IAccountDAO {
	public static enum EnumRoleID {
		GUEST, ADMIN, EMPLOYEE,
	}

	public Account findAccountByEmail(String login);

	public Account getAccount(int id);

	public Role getRoleViaEnum(EnumRoleID roleID);

	public boolean addAccountToDB(Account acc);

	public List<Account> listAccountWithRole(EnumRoleID roleID);

	public List<Account> listAccounts();

	public boolean updateAccount(Account account);

	public boolean deleteAccount(Account account);

}
