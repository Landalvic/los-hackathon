import React from "react";
import { connect } from "react-redux";
import { FilAriane } from "../../commun/FilAriane";
import { initPost, initPostFile, telechargerFichier } from "../../_utils";
import { WS_CONTEXT_PATH } from "../../_properties";
import { waitingTrue, waitingFalse } from "../../commun/GeneralReducer";
import ValiderOuTelecharger from "../commun/ValiderOuTelecharger";
import { setMesures, setConcepts } from "../ReferentielReducer";

class ConceptChargerFichier extends React.Component {
  constructor(props) {
    super(props);
    this.onClickValider = this.onClickValider.bind(this);
    this.onClickTelecharger = this.onClickTelecharger.bind(this);
    this.onClickCommun = this.onClickCommun.bind(this);
    this.onChangeFile = this.onChangeFile.bind(this);
    this.state = {
      lienFichier: null
    };
  }

  onChangeFile(e) {
    this.props.waitingTrue();
    fetch(WS_CONTEXT_PATH + "/fichier/temp/chargement", initPostFile(e.target.files[0]))
      .then(response => response.json())
      .then(json => {
        this.props.waitingFalse();
        this.setState({
          lienFichier: json.lienFichier
        });
      })
      .catch(error => {
        console.log(error);
      });
  }

  onClickValider() {
    const params = this.onClickCommun();
    fetch(WS_CONTEXT_PATH + "/concept/chargement/fichier/valider", initPost(params))
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
    fetch(WS_CONTEXT_PATH + "/concept/chargement/fichier/telecharger", initPost(params))
      .then(response => response.text())
      .then(text => {
        this.props.waitingFalse();
        telechargerFichier(text, "concepts.ttl");
      })
      .catch(error => {
        console.log(error);
      });
  }

  onClickCommun() {
    this.props.waitingTrue();
    const params = {
      lienFichier: this.state.lienFichier
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
      { libelle: "Créer des concepts" }
    ];
    return (
      <div>
        <FilAriane filAriane={filAriane} titre="Créer des concepts" />

        <div className="large-12 large-centered columns">
          <fieldset>
            <legend>Chargement</legend>
            <div className="row">
              <div className="large-4 columns">
                <label>
                  Fichier à charger <span className="required">*</span>{" "}
                </label>
                <input onChange={this.onChangeFile} type="file" />
              </div>
              <div className="large-4 columns" />
            </div>
            {this.state.lienFichier ? <ValiderOuTelecharger onClickValider={this.onClickValider} onClickTelecharger={this.onClickTelecharger} /> : null}
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

export default connect(mapStateToProps, mapDispatchToProps)(ConceptChargerFichier);
