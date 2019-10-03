import {combineReducers} from "redux";
import chatReducer from "./chat/ChatReducer";
import configReducer from "./config/ConfigReducer";
import boardReducer from "./board/BoardReducer";

export default combineReducers({
    chatReducer, configReducer, boardReducer
});