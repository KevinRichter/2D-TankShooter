package a4;

public interface ICollider {
	public void handleCollision(ICollider obj);
	public boolean collidesWith(ICollider otherObj);
}
