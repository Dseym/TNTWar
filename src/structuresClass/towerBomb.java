package structuresClass;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;

import tntWar.structure;
import tntWar.team;

public class towerBomb extends structure {
	
	public static List<String> description = Arrays.asList("�������� ���������, ���� ����� ������� ����������", "(��������� �� ���� �������, �����, ������� �����)");
	public static int cost = 1300;
	public static String name = "����� ��������";
	public static String id = "TowerBomb";

	public towerBomb(Location loc, team team) {
		
		super(600, loc, id, team, cost);
		
	}
	
}
