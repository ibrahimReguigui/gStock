package Ibrahim.SpringBoot.model;

public enum AgentStatus {
    AWAITING_CONFIRMATION ("Awaiting Confirmation"),
    CONFIRMED("Confirmed");

    private final String status;
    AgentStatus(String status){
        this.status=status;
    }
    public String getStatus(){
        return status;
    }
}
