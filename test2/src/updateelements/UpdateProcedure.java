package updateelements;

public enum UpdateProcedure {
	SUR,CCU;
	
    public static UpdateProcedure fromInteger(int x) {
        switch(x) {
        case 0:
            return SUR;
        case 1:
            return CCU;
        }
        return null;
    }
    
    public static int toInteger(UpdateProcedure updateProcedure){
        switch(updateProcedure) {
        case SUR:
            return 0;
        case CCU:
            return 1;
        }
        return 1;
    }
}
