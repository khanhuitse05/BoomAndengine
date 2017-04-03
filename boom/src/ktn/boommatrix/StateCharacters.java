package ktn.boommatrix;

public enum StateCharacters {
	stop(0), up(1), left(2), right(3), down(4);
	int value;
	private StateCharacters(int value) {
		// TODO Auto-generated constructor stub
		this.value = value;
	}
}
