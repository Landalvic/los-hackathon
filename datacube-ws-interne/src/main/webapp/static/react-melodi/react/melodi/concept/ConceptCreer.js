import React from "react";
import { connect } from "react-redux";
import { FilAriane } from "../../commun/FilAriane";
import { initPost, initPostFile, handleChangeState, telechargerFichier } from "../../_utils";
import { WS_CONTEXT_PATH } from "../../_properties";
import { waitingTrue, waitingFalse } from "../../commun/GeneralReducer";
import ValiderOuTelecharger from "../commun/ValiderOuTelecharger";
import { setMesures, setConcepts } from "../ReferentielReducer";

class ConceptCreer extends React.Component {
  constructor(props) {
    super(props);
    this.onClickValider = this.onClickValider.bind(this);
    this.onClickTelecharger = this.onClickTelecharger.bind(this);
    this.onClickCommun = this.onClickCommun.bind(this);
    this.handleChange = handleChangeState(this);
    this.state = {
      code: null,
      libelleFr: null,
      libelleEn: null,
      isMesure: false
    };
  }

  onClickValider() {
    const params = this.onClickCommun();
    fetch(WS_CONTEXT_PATH + "/concept/chargement/valider", initPost(params))
      .then(response => {
        this.props.waitingFalse();
        this.props.setMesures([]);
        this.props.setConcepts([]);
      })
      .catch(error => {
        console.log(error);
      });
  }

  onClickTelecharger() {
    const params = this.onClickCommun();
    fetch(WS_CONTEXT_PATH + "/concept/chargement/telecharger", initPost(params))
      .then(response => response.text())
      .then(text => {
        this.props.waitingFalse();
        telechargerFichier(text, this.state.code + ".ttl");
      })
      .catch(error => {
        console.log(error);
      });
  }

  onClickCommun() {
    this.props.waitingTrue();
    const params = {
      code: this.state.code,
      libelleFr: this.state.libelleFr,
      libelleEn: this.state.libelleEn,
      mesure: this.state.isMesure
    };
    return params;
  }

  render() {
    const { context } = this.props;
    const filAriane = [
      {
        href: context + "/los/react/melodi",
        libelle: "MELODI"
      },
      { libelle: "Créer un concept" }
    ];
    return (
      <div>
        <FilAriane filAriane={filAriane} titre="Créer un concept" />

        <div className="large-12 large-centered columns">
          <fieldset>
            <legend>Chargement</legend>
            <div className="row">
              <div className="large-4 columns">
                <label>Code :</label>
                <input type="text" name="code" value={this.state.code} onChange={this.handleChange} />
              </div>
            </div>
            <div className="row">
              <div className="large-4 columns">
                <label>Libellé français :</label>
                <input type="text" name="libelleFr" value={this.state.libelleFr} onChange={this.handleChange} />
              </div>
            </div>
            <div className="row">
              <div className="large-4 columns">
                <label>Libellé anglais :</label>
                <input type="text" name="libelleEn" value={this.state.libelleEn} onChange={this.handleChange} />
              </div>
            </div>
            <div className="row">
              <div className="large-4 columns">
                <label>Mesure :</label>
                <input type="checkbox" name="isMesure" checked={this.state.isMesure} onChange={this.handleChange} />
              </div>
            </div>
            {this.state.code ? <ValiderOuTelecharger onClickValider={this.onClickValider} onClickTelecharger={this.onClickTelecharger} /> : null}
          </fieldset>
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ generalReducer }) => {
  return {
    context: generalReducer.context
  };
};

const mapDispatchToProps = dispatch => {
  return {
    waitingTrue: () => {
      dispatch(waitingTrue());
    },
    waitingFalse: () => {
      dispatch(waitingFalse());
    },
    setConcepts: concepts => {
      dispatch(setConcepts(concepts));
    },
    setMesures: mesures => {
      dispatch(setMesures(mesures));
    }
  };
};

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ConceptCreer);
