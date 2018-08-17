import React, { PropTypes } from "react";
import { connect } from "react-redux";
import Waiting from "./Waiting";
import Header from "./Header";
import { Footer } from "./Footer";
import { Route, Switch, withRouter } from "react-router-dom";
import ChargementFichierAgrege from "../melodi/ChargementFichierAgrege";
import ChargementFichierIndividuel from "../melodi/ChargementFichierIndividuel";
import ValidationSource from "../melodi/ValidationSource";
import ValidationFichierIndividuel from "../melodi/ValidationFichierIndividuel";
import Visualisation from "../melodi/Visualisation";
import RechercheDataflow from "../melodi/RechercheDataflow";
import Dataflow from "../melodi/dataflow/Dataflow";
import CodeListeCreer from "../melodi/codeliste/CodeListeCreer";
import CodeListeRechercher from "../melodi/codeliste/CodeListeRechercher";
import CodeListeVisualiser from "../melodi/codeliste/CodeListeVisualiser";
import ConceptCreer from "../melodi/concept/ConceptCreer";
import ConceptRechercher from "../melodi/concept/ConceptRechercher";
import ConceptVisualiser from "../melodi/concept/ConceptVisualiser";
import DSD from "../melodi/dsd/DSD";
import DSDRechercher from "../melodi/dsd/DSDRechercher";
import { setContext } from "./GeneralReducer";

class Main extends React.Component {
  constructor(props) {
    super(props);
  }

  componentWillMount() {
    if (this.props.context === null) {
      this.props.setContext(window.location.pathname.substring(0, window.location.pathname.indexOf("/los/react/")));
    }
  }

  render() {
    const { context } = this.props;

    return (
      <div className="QF01">
        <Header />
        <main id="contenu" className="main react" role="main">
          <Waiting />
          <Switch>
            <Route exact path={context + "/los/react/melodi/accueil"} component={ChargementFichierAgrege} />
            <Route path={context + "/los/react/melodi/chargement/donnees-agregees"} component={ChargementFichierAgrege} />
            <Route path={context + "/los/react/melodi/validation/donnees-agregees"} component={ValidationSource} />
            <Route path={context + "/los/react/melodi/chargement/donnees-individuelles"} component={ChargementFichierIndividuel} />
            <Route path={context + "/los/react/melodi/validation/donnees-individuelles"} component={ValidationFichierIndividuel} />
            <Route path={context + "/los/react/melodi/visualisation"} component={Visualisation} />
            <Route path={context + "/los/react/melodi/recherche/dataflows"} component={RechercheDataflow} />
            <Route path={context + "/los/react/melodi/dataflow/:dataflow?"} component={Dataflow} />
            <Route path={context + "/los/react/melodi/code-liste/chargement"} component={CodeListeCreer} />
            <Route path={context + "/los/react/melodi/code-liste/recherche"} component={CodeListeRechercher} />
            <Route path={context + "/los/react/melodi/code-liste/:codeListe?"} component={CodeListeVisualiser} />
            <Route path={context + "/los/react/melodi/concept/chargement"} component={ConceptCreer} />
            <Route path={context + "/los/react/melodi/concept/recherche"} component={ConceptRechercher} />
            <Route path={context + "/los/react/melodi/concept/:concept?"} component={ConceptVisualiser} />
            <Route path={context + "/los/react/melodi/dsd/chargement/:onglet?/:dsd?"} component={DSD} />
            <Route path={context + "/los/react/melodi/dsd/recherche"} component={DSDRechercher} />
          </Switch>
        </main>
        <Footer />
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
    setContext: context => {
      dispatch(setContext(context));
    }
  };
};

export default withRouter(
  connect(
    mapStateToProps,
    mapDispatchToProps
  )(Main)
);
