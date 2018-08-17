import React from "react";
import { connect } from "react-redux";

export class FilAriane extends React.Component {
  constructor(props) {
    super(props);
  }

  render() {
    const breadcrum = this.props.filAriane.map((item, index) => {
      if (index + 1 === this.props.filAriane.legnth) {
        return (
          <li key={index}>
            <a className="current">{item.libelle}</a>
          </li>
        );
      } else {
        return (
          <li key={index}>
            <a href={item.href}>{item.libelle}</a>
          </li>
        );
      }
    });
    return (
      <div>
        <div id="sous-titre" className="sous-titre">
          {this.props.titre}
        </div>
        <ul className="breadcrumbs">
          <li>
            <a href="/web4g/accueil">Accueil</a>
          </li>
          {breadcrum}
        </ul>
      </div>
    );
  }
}
