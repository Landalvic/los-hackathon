import React from "react";
import { connect } from "react-redux";
import { Route, Switch } from "react-router-dom";
import { FilAriane } from "../../commun/FilAriane";
import { initGet, initPost, telechargerFichier } from "../../_utils";
import DSDCreer from "./DSDCreer";
import DSDCreerDimensions from "./DSDCreerDimensions";
import DSDCreerMesures from "./DSDCreerMesures";
import DSDCreerSlices from "./DSDCreerSlices";
import DSDCreerAttributs from "./DSDCreerAttributs";
import ValiderOuTelecharger from "../commun/ValiderOuTelecharger";
import { setDSD } from "../ChargementReducer";
import { waitingTrue, waitingFalse } from "../../commun/GeneralReducer";
import { WS_CONTEXT_PATH } from "../../_properties";
import { setListeDSD } from "../VisualisationReducer";

class DSD extends React.Component {
  constructor(props) {
    super(props);
    this.onClickValider = this.onClickValider.bind(this);
    this.onClickTelecharger = this.onClickTelecharger.bind(this);
    this.onClickCommun = this.onClickCommun.bind(this);
  }

  changementDsd = () => {
    if (this.props.match.params.dsd) {
      this.props.waitingTrue();
      fetch(WS_CONTEXT_PATH + "/dsd/" + this.props.match.params.dsd, initGet())
        .then(response => response.json())
        .then(json => {
          this.props.waitingFalse();
          this.props.setDSD(json);
        })
        .catch(error => {
          console.log(error);
        });
    } else {
      this.props.setDSD(null);
    }
  };

  componentDidMount() {
    this.changementDsd();
  }

  componentDidUpdate(prevProps) {
    if (prevProps.match.params.dsd !== this.props.match.params.dsd) {
      this.changementDsd();
    }
  }

  onClickValider() {
    const params = this.onClickCommun();
    fetch(WS_CONTEXT_PATH + "/dsd/chargement/valider", initPost(params))
      .then(response => {
        this.props.waitingFalse();
        this.props.setListeDSD([]);
      })
      .catch(error => {
        console.log(error);
      });
  }

  onClickTelecharger() {
    const params = this.onClickCommun();
    fetch(WS_CONTEXT_PATH + "/dsd/chargement/telecharger", initPost(params))
      .then(response => response.text())
      .then(text => {
        this.props.waitingFalse();
        telechargerFichier(text, this.props.dsd.code + ".ttl");
      })
      .catch(error => {
        console.log(error);
      });
  }

  onClickCommun() {
    this.props.waitingTrue();
    const params = this.props.dsd;
    return params;
  }

  render() {
    const { context } = this.props;
    const filAriane = [
      {
        href: "/los/react/melodi",
        libelle: "MELODI"
      },
      { libelle: "Créer une DSD" }
    ];
    return (
      <div>
        <FilAriane filAriane={filAriane} titre="Créer une DSD" />
        <Switch>
          <Route path={context + "/los/react/melodi/dsd/chargement/generalites/:dsd?"} component={DSDCreer} />
          <Route path={context + "/los/react/melodi/dsd/chargement/dimensions/:dsd?"} component={DSDCreerDimensions} />
          <Route path={context + "/los/react/melodi/dsd/chargement/mesures/:dsd?"} component={DSDCreerMesures} />
          <Route path={context + "/los/react/melodi/dsd/chargement/slices/:dsd?"} component={DSDCreerSlices} />
          <Route path={context + "/los/react/melodi/dsd/chargement/attributs/:dsd?"} component={DSDCreerAttributs} />
        </Switch>
        {this.props.dsd.code ? <ValiderOuTelecharger onClickValider={this.onClickValider} onClickTelecharger={this.onClickTelecharger} /> : null}
      </div>
    );
  }
}

const mapStateToProps = ({ generalReducer, chargementReducer }) => {
  return {
    context: generalReducer.context,
    dsd: chargementReducer.dsd
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
    setDSD: dsd => {
      dispatch(setDSD(dsd));
    },
    setListeDSD: dsd => {
      dispatch(setListeDSD(dsd));
    }
  };
};

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(DSD);
