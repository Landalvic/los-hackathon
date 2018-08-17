import React from "react";
import { connect } from "react-redux";

export default class ValiderOuTelecharger extends React.Component {
  constructor(props) {
    super(props);
  }

  render() {
    return (
      <div className="row">
        <div className="large-3 columns">
          <input type="submit" value="Valider" onClick={this.props.onClickValider} />
        </div>
        {this.props.onClickTelecharger ? (
          <div className="large-3 columns">
            <input type="submit" value="Télécharger Turtle" onClick={this.props.onClickTelecharger} />
          </div>
        ) : null}
      </div>
    );
  }
}
