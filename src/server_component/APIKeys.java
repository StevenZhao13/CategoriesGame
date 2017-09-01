package server_component;

public class APIKeys {
    public static final String 
            KEY_TYPE = "type",
    
    TYPE_INITATE_PARTY = "InitiateParty",
    TYPE_CONFIRM_INIT = "ConfirmInit",
    TYPE_JOIN_PARTY = "JoinParty",
    TYPE_CONFIRM_JOIN = "ConfirmJoin",
    TYPE_START_PARTY = "StartParty",
    TYPE_SEND_ANSWER_LIST = "SendAnswerList",
    TYPE_SEND_COMPLETE_VOTE_LIST = "SendCompletedVoteList",
    TYPE_RESTART_PARTY = "RestartParty",
    TYPE_TERMINATE_PARTY = "TerminateParty",
    TYPE_SEND_UPDATED_PLAYER_LIST = "SendUpdatedPlayerList",
    TYPE_SEND_LETTER_N_CATEGORY_LIST = "SendLetterNCategoryList",
    TYPE_SEND_VOTE_LIST = "SendVoteList",
    TYPE_SEND_RESULT_LIST = "SendResultList",
    TYPE_CONFIRM_RESTART = "ConfirmRestart",
    TYPE_CONFIRM_TERMINATE = "ConfirmTerminate",
    TYPE_EXCEPTION = "Exception";

    public static final String
            KEY_NAME = "name",
    KEY_DEVICE_ID = "deviceID",
    KEY_PARTY_ID = "partyID";

    public static final String
            KEY_ANSWER_LIST = "answerList",
    KEY_ID_LIST = "IDList",
    KEY_NAME_LIST = "nameList",
    KEY_CATEGORY_LIST = "categoryList";

    public static final String
    KEY_ERROR_TYPE = "errorType",
    KEY_ERROR_MESSAGE = "errorMessage";

}
