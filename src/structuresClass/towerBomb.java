package structuresClass;

import org.bukkit.Location;

import tntWar.structure;
import tntWar.team;

public class towerBomb extends structure {
	
	public static String description = "�������� ��������� ����, ���� ����� ������� ����������(��������� �� ���� �������, ����� ����, ������� �����)";
	public static int cost = 1300;
	public static String name = "����� ��������";
	public static String id = "TowerBomb";

	public towerBomb(Location loc, team team) {
		
		super(600, loc, id, team, cost);
		
	}
	
}
