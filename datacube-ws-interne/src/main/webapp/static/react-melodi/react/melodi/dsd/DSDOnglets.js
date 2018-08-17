import React from "react";
import { connect } from "react-redux";
import { Link } from "react-router-dom";

class DSDOnglets extends React.Component {
  constructor(props) {
    super(props);
  }

  render() {
    const { context, code } = this.props;
    const urlDSD = this.props.dsdParams ? "/" + this.props.dsdParams : "";
    const onglets = code
      ? [
          { url: context + "/los/react/melodi/dsd/chargement/generalites" + urlDSD, libelle: "Généralités" },
          {
            url: context + "/los/react/melodi/dsd/chargement/dimensions" + urlDSD,
            libelle: "Dimensions"
          },
          {
            url: context + "/los/react/melodi/dsd/chargement/mesures" + urlDSD,
            libelle: "Mesures"
          },
          {
            url: context + "/los/react/melodi/dsd/chargement/slices" + urlDSD,
            libelle: "Slices"
          },
          {
            url: context + "/los/react/melodi/dsd/chargement/attributs" + urlDSD,
            libelle: "Attributs"
          }
        ]
      : [{ url: context + "/los/react/melodi/dsd/chargement/generalites" + urlDSD, libelle: "Généralités" }];
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

const mapStateToProps = ({ generalReducer, chargementReducer }) => {
  return {
    context: generalReducer.context,
    code: chargementReducer.dsd.code
  };
};

const mapDispatchToProps = dispatch => {
  return {};
};

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(DSDOnglets);
