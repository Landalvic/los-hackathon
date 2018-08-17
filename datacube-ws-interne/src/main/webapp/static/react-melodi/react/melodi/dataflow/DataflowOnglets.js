import React from "react";
import { connect } from "react-redux";
import { Link } from "react-router-dom";

class DataflowOnglets extends React.Component {
  constructor(props) {
    super(props);
  }

  render() {
    const dataflow = this.props.dataflow ? this.props.dataflow : "";
    const onglets = this.props.dataflow
      ? [
          { url: "/los/react/melodi/dataflow/" + dataflow, libelle: "Dataflow" },
          { url: "/los/react/melodi/dataflow/" + dataflow + "/series", libelle: "Séries" }
        ]
      : [{ url: "/los/react/melodi/dataflow/" + dataflow, libelle: "Dataflow" }];
    const lis = onglets.map((onglet, index) => {
      if (index + 1 === this.props.active) {
        return (
          <li key={index} className="tab active">
            {onglet.libelle}
          </li>
        );
      } else {
        return (
          <li key={index} className="tab">
            <Link to={onglet.url}>{onglet.libelle}</Link>
          </li>
        );
      }
    });
    return <ul className="tabs">{lis}</ul>;
  }
}

const mapStateToProps = () => {
  return {};
};

const mapDispatchToProps = dispatch => {
  return {};
};

export default connect(mapStateToProps, mapDispatchToProps)(DataflowOnglets);
