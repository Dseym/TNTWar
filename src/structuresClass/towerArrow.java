package structuresClass;

import org.bukkit.Location;

import tntWar.structure;
import tntWar.team;

public class towerArrow extends structure {
	
	public static String description = "�������� �� ����� �������� �� ���������� 40 ������";
	public static int cost = 900;
	public static String name = "����� ��������";
	public static String id = "TowerArrow";

	public towerArrow(Location loc, team team) {
		
		super(500, loc, id, team, cost);
		
	}
	
}
