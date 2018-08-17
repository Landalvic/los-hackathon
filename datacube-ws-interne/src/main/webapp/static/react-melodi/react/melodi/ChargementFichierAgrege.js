import React from "react";
import { connect } from "react-redux";
import { FilAriane } from "../commun/FilAriane";
import ChargementFichier from "./ChargementFichier";
import { initPostFile, initGet, initPost } from "../_utils";
import { WS_CONTEXT_PATH } from "../_properties";
import { waitingTrue, waitingFalse } from "../commun/GeneralReducer";
import { setInfosFichier, setColonnes, changeChoixGestionnaire } from "./ChargementReducer";

class ChargementFichierAgrege extends React.Component {
  constructor(props) {
    super(props);
  }

  render() {
    const filAriane = [
      {
        href: "/los/react/melodi",
        libelle: "MELODI"
      },
      { libelle: "Chargement d'un Fichier agrégé" }
    ];
    const urlValidation = WS_CONTEXT_PATH + "/chargement/fichier/donnees-agregees/valider";
    const urlTelechargement = WS_CONTEXT_PATH + "/chargement/fichier/donnees-agregees/telecharger-turtle";
    return (
      <div>
        <FilAriane filAriane={filAriane} titre="Chargement d'un Fichier agrégé" />
        <ChargementFichier urlValidation={urlValidation} urlTelechargement={urlTelechargement} />
      </div>
    );
  }
}

const mapStateToProps = ({ chargementReducer }) => {
  return {};
};

const mapDispatchToProps = dispatch => {
  return {};
};

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ChargementFichierAgrege);
