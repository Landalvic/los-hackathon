import React from "react";
import { connect } from "react-redux";
import { Route, Switch } from "react-router-dom";
import { FilAriane } from "../../commun/FilAriane";
import { initGet } from "../../_utils";
import { waitingTrue, waitingFalse } from "../../commun/GeneralReducer";
import DataflowVisualisation from "./DataflowVisualisation";
import DataflowSeries from "./DataflowSeries";

class Dataflow extends React.Component {
  constructor(props) {
    super(props);
  }

  componentDidMount() {}

  render() {
    const filAriane = [
      {
        href: "/los/react/melodi",
        libelle: "MELODI"
      },
      { libelle: "Chargement d'un dataflow" }
    ];
    return (
      <div>
        <FilAriane filAriane={filAriane} titre="Dataflow" />
        <Switch>
          <Route exact path="/los/react/melodi/dataflow/:dataflow?" component={DataflowVisualisation} />
          <Route path="/los/react/melodi/dataflow/:dataflow/series" component={DataflowSeries} />
        </Switch>
      </div>
    );
  }
}

const mapStateToProps = () => {
  return {};
};

const mapDispatchToProps = dispatch => {
  return {};
};

export default connect(mapStateToProps, mapDispatchToProps)(Dataflow);
