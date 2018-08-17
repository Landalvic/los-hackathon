import React from "react";
import { connect } from "react-redux";
import { Link } from "react-router-dom";

export class Header extends React.Component {
  constructor(props) {
    super(props);
    this.generateMelodiMenu = this.generateMelodiMenu.bind(this);
  }

  generateMelodiMenu() {
    const { context } = this.props;
    return (
      <ul className="title-area">
        <li className="name">
          <Link to={context + "/los/react/melodi/accueil"}>Accueil</Link>
        </li>
        <li className="divider" />
        <li className="has-dropdown">
          <a href="#">Données Agrégés</a>
          <ul className="dropdown">
            <li>
              <Link to={context + "/los/react/melodi/chargement/donnees-agregees"}>Charger</Link>
            </li>
            <li>
              <Link to={context + "/los/react/melodi/visualisation"}>Visualiser</Link>
            </li>
          </ul>
        </li>
        <li className="has-dropdown">
          <a href="#">DSD</a>
          <ul className="dropdown">
            <li>
              <Link to={context + "/los/react/melodi/dsd/chargement/generalites"}>Créer</Link>
            </li>
            <li>
              <Link to={context + "/los/react/melodi/dsd/recherche"}>Rechercher</Link>
            </li>
          </ul>
        </li>
        <li className="has-dropdown">
          <a href="#">Concept</a>
          <ul className="dropdown">
            <li>
              <Link to={context + "/los/react/melodi/concept/chargement"}>Créer</Link>
            </li>
            <li>
              <Link to={context + "/los/react/melodi/concept/recherche"}>Rechercher</Link>
            </li>
          </ul>
        </li>
        <li className="has-dropdown">
          <a href="#">Code liste</a>
          <ul className="dropdown">
            <li>
              <Link to={context + "/los/react/melodi/code-liste/chargement"}>Créer</Link>
            </li>
            <li>
              <Link to={context + "/los/react/melodi/code-liste/recherche"}>Rechercher</Link>
            </li>
          </ul>
        </li>
      </ul>
    );
  }

  render() {
    const { context } = this.props;
    const menu = this.generateMelodiMenu();
    return (
      <div>
        <div className="row.collapse">
          <h1>LOS Chargement</h1>
        </div>

        <div className="large-12 columns main-menu">
          <nav className="top-bar " data-topbar="data-topbar">
            <section className="top-bar-section">{menu}</section>
          </nav>
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
  return {};
};

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Header);
