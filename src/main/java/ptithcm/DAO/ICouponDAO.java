package ptithcm.DAO;

import java.util.List;

import ptithcm.entity.Account;
import ptithcm.entity.Coupon;
import ptithcm.entity.Product;

public interface ICouponDAO {
	List<Coupon> listCoupons();

	List<Coupon> listCoupons(int accountId);

	List<Product> listProductWithCoupon(int limit);

	Account FetchAccountCoupon(Account acc);

	boolean add(Coupon coupon);

	boolean update(Coupon coupon);

	boolean delete(Coupon coupon);

	Coupon getCoupon(int id);

}
