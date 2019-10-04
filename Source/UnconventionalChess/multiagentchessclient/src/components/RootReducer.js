import {combineReducers} from "redux";
import chatReducer from "./chat/ChatReducer";
import configReducer from "./config/ConfigReducer";
import boardReducer from "./board/BoardReducer";
import websocketReducer from "./websocket/WebsocketReducer";

export default combineReducers({
    chatReducer, configReducer, boardReducer, websocketReducer
});