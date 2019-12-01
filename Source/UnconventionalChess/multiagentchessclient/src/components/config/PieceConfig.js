import React from 'react'
import {connect} from "react-redux";

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
    return <form>
        <h3>Configuring Piece at {props.configuringSquare}</h3>
        <ul style={{listStyle: "none"}}>
            <li>
                <label>Name</label>
                <input type="text"/>
            </li>
            <li>
                <label>Personality Type</label>
                <select>
                    <option>None</option>
                </select>
            </li>
            <li>
                <button type="button" onClick={props.saveConfig}>Save</button>
            </li>
        </ul>
    </form>
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
            props.humanPlaysAsWhite)
            ? <PieceConfigForm props={props}/>
            : <NotConfigurableMessage props={props}/>
        }
    </div>
};

function mapStateToProps(state) {
    return {
        humanPlays: state.configReducer.humanPlays,
        humanPlaysAsWhite: state.configReducer.humanPlaysAsWhite,
        configuringSquare: state.configReducer.configuringSquare,
    };
}

const mapDispatchToProps = {
    saveConfig: () => (console.log("Saved config"))
};

export default connect(mapStateToProps, mapDispatchToProps)(PieceConfig)