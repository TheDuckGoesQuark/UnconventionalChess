import {combineReducers} from "redux";
import chatReducer from "./chat/ChatReducer";
import configReducer from "./config/ConfigReducer";

export default combineReducers({
    chatReducer, configReducer
});