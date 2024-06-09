package ptithcm.DAO;

import java.util.List;

import ptithcm.entity.Wishlist;

public interface IWishlistDAO {
	boolean insertWishlist(Wishlist wishlist);

	boolean deleteWishlist(Wishlist wishlist);

	boolean updateWishlist(Wishlist wishlist);

	Wishlist getWishlist(int accountId, int productId);

	List<Wishlist> Wishlist(int accountId);

	int removeWishlist(int accountId);

	List<Wishlist> getWishlist(int accountId);

}
