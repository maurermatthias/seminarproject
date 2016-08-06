package dbentities;

public enum Usergroup {
	UNKNOWN, STUDENT, TEACHER, ADMINISTRATOR;
	
    public static Usergroup fromInteger(int x) {
        switch(x) {
        case 0:
            return UNKNOWN;
        case 1:
            return STUDENT;
        case 2:
            return TEACHER;
        case 3:
            return ADMINISTRATOR;
        }
        return null;
    }
    
    public static int toInteger(Usergroup usergroup){
        switch(usergroup) {
        case UNKNOWN:
            return 0;
        case STUDENT:
            return 1;
        case TEACHER:
            return 2;
        case ADMINISTRATOR:
            return 3;
        }
        return 0;
    }

}
