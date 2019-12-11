import React from 'react'
import {connect} from "react-redux";
import {pieceConfigSaved, updatePieceConfigName, updatePieceConfigPersonality} from "./ConfigActions";

/**
 * Determines if the piece at the given square can be configured by a human
 * @param square square that might be configurable
 * @param humanPlays if the human is playing at all
 * @param humanPlaysAsWhite if the human is playing as white
 * @returns {boolean}
 */
const isConfigurable = (square, humanPlays, humanPlaysAsWhite) => {
    if (!humanPlays) return true;
    let pieceIsWhite = parseInt(square.charAt(1)) <= 2;
    return pieceIsWhite ? !humanPlaysAsWhite : humanPlaysAsWhite
};

const PieceConfigForm = ({props}) => {
    let currentConfig = props.pieceConfigs[props.configuringSquare];

    return <div>
        <h3>Configuring piece at {props.configuringSquare.toUpperCase()}</h3>
        <ul style={{listStyle: "none"}}>
            <li>
                <label>Name
                    <input type="text" value={currentConfig ? currentConfig.name : ''}
                           onChange={event => props.updatePieceName(event.target.value)}/>
                </label>
            </li>
            <li>
                <label>Personality Type
                    <select value={currentConfig ? currentConfig.personality : ''}
                            onChange={(e) => props.updatePiecePersonality(props.personalityTypes[e.target.value])}>
                        <option value={''}>Select...</option>
                        {props.personalityTypes.map((pt, index) => <option key={pt.name}
                                                                           value={index}>{pt.name}</option>)}
                    </select>
                </label>
            </li>
            <li>
                <button type="button" onClick={props.savePieceConfig}>Save</button>
            </li>
        </ul>
    </div>
};

const NotConfigurableMessage = ({props}) => {
    return <h3>Piece at {props.configuringSquare} is Human Controlled,
        cannot be configured</h3>
};

const PieceConfig = (props) => {
    return <div>
        {isConfigurable(
            props.configuringSquare,
            props.humanPlays,
            props.humanPlaysAsWhite) && props.personalityTypes // check personality types has loaded
            ? <PieceConfigForm props={props}/>
            : <NotConfigurableMessage props={props}/>
        }
    </div>
};

function mapStateToProps(state) {
    return {
        humanPlays: state.configReducer.humanPlays,
        humanPlaysAsWhite: state.configReducer.humanPlaysAsWhite,
        pieceConfigs: state.configReducer.pieceConfigs,
        configuringSquare: state.configReducer.configuringSquare,
        personalityTypes: state.configReducer.personalityTypes,
    };
}

const mapDispatchToProps = {
    savePieceConfig: pieceConfigSaved,
    updatePieceName: updatePieceConfigName,
    updatePiecePersonality: updatePieceConfigPersonality
};

export default connect(mapStateToProps, mapDispatchToProps)(PieceConfig)