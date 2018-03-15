package inf101.v18.rogue101.objects;

public interface IEquipment extends IItem {
	int modifyAttack();
	
	int modifyDefence();
	
	int modifyViewRange();
	
	int modifyMaxHealth();
}
