import React from "react";
import { connect } from "react-redux";
import { FilAriane } from "../commun/FilAriane";
import { initPostFile, initGet, initPost, telechargerFichier } from "../_utils";
import { WS_CONTEXT_PATH } from "../_properties";
import { waitingTrue, waitingFalse } from "../commun/GeneralReducer";
import { setInfosFichier, setColonnes, changeChoixGestionnaire, changeChoixParDefaut, setDatacube } from "./ChargementReducer";
import { fetchDatacubes, selectionDatacube } from "./VisualisationReducer";

class ChargementFichier extends React.Component {
  constructor(props) {
    super(props);
    this.onChangeFile = this.onChangeFile.bind(this);
    this.onChangeGestionnaire = this.onChangeGestionnaire.bind(this);
    this.onChangeParDefaut = this.onChangeParDefaut.bind(this);
    this.onChangeDatacube = this.onChangeDatacube.bind(this);
    this.onClickValider = this.onClickValider.bind(this);
    this.onClickTelecharger = this.onClickTelecharger.bind(this);
    this.onClickCommun = this.onClickCommun.bind(this);
  }

  componentDidMount() {
    if (this.props.colonnes.length == 0) {
      this.props.waitingTrue();
      fetch(WS_CONTEXT_PATH + "/chargement/colonnes", initGet())
        .then(response => response.json())
        .then(json => {
          this.props.waitingFalse();
          this.props.setColonnes(json);
        })
        .catch(error => {
          console.log(error);
        });
    }
    this.props.fetchDatacubes();
  }

  onChangeFile(e) {
    this.props.waitingTrue();
    fetch(WS_CONTEXT_PATH + "/chargement/fichier", initPostFile(e.target.files[0]))
      .then(response => response.json())
      .then(json => {
        this.props.setInfosFichier(json);
        this.props.waitingFalse();
      })
      .catch(error => {
        console.log(error);
      });
  }

  onChangeGestionnaire(index, e) {
    this.props.changeChoixGestionnaire(e.target.value !== "null" ? e.target.value : null, index);
  }

  onChangeParDefaut(index, e) {
    this.props.changeChoixParDefaut(e.target.value !== "null" ? e.target.value : null, index);
  }

  onChangeDatacube(e) {
    const datacube = this.props.datacubes.find(datacube => datacube.iri === e.target.value);
    this.props.selectionDatacube(datacube, setDatacube);
  }

  onClickValider() {
    const params = this.onClickCommun();
    fetch(this.props.urlValidation, initPost(params))
      .then(response => this.props.waitingFalse())
      .catch(error => {
        console.log(error);
      });
  }

  onClickTelecharger() {
    const params = this.onClickCommun();
    fetch(this.props.urlTelechargement, initPost(params))
      .then(response => response.text())
      .then(text => {
        this.props.waitingFalse();
        telechargerFichier(text, this.props.datacubeSelected.code + "-dataset.ttl");
      })
      .catch(error => {
        console.log(error);
      });
  }

  onClickCommun() {
    this.props.waitingTrue();
    const choixGestionnaire = this.props.infosFichier.colonnes.map((colonneWrapper, index) => {
      return {
        nomColonne: colonneWrapper.nomColonne,
        choix: this.props.choixGestionnaire[index]
      };
    });
    const choixParDefaut = this.props.datacubeSelected.components
      .map((component, index) => {
        if (this.props.choixGestionnaire.includes(component.iri)) {
          return null;
        } else {
          return {
            iri: component.iri,
            choix: this.props.choixParDefaut[index]
          };
        }
      })
      .filter(component => component);
    const params = {
      lienFichier: this.props.infosFichier.lienFichier,
      datacube: this.props.datacubeSelected.iri,
      choixGestionnaire,
      choixParDefaut
    };
    return params;
  }

  render() {
    let tableau = null;
    let boutonValidation = false;
    const optionsDatacubes = this.props.datacubes.map(datacube => <option value={datacube.iri}>{datacube.libelleFr}</option>);
    if (this.props.infosFichier && this.props.datacubeSelected) {
      boutonValidation = true;
      const body = this.props.infosFichier.colonnes.map((colonneWrapper, index) => {
        let proposition = null;
        if (colonneWrapper.colonne) {
          let lienVariable = null;
          if (colonneWrapper.colonne.iriVariable) {
            lienVariable = (
              <div>
                <a href={colonneWrapper.colonne.iriVariable}>{colonneWrapper.colonne.iriVariable}</a>
              </div>
            );
          }
          proposition = (
            <td key={index}>
              <div>{colonneWrapper.colonne.libelle}</div>
              {lienVariable}
            </td>
          );
        } else {
          boutonValidation = boutonValidation && this.props.choixGestionnaire[index];
          proposition = (
            <td className="required" key={index}>
              Non trouvé !
            </td>
          );
        }
        const options = this.props.datacubeSelected.components
          .filter(component => !this.props.choixGestionnaire.includes(component.iri) || this.props.choixGestionnaire[index] === component.iri)
          .map((component, indexOption) => {
            if (this.props.choixGestionnaire[index] === component.iri) {
              return (
                <option selected="selected" value={component.iri}>
                  {component.libelleFr}
                </option>
              );
            } else {
              return <option value={component.iri}>{component.libelleFr}</option>;
            }
          });
        return (
          <tr key={index}>
            <td>{colonneWrapper.nomColonne}</td>
            {proposition}
            <td>
              <select onChange={e => this.onChangeGestionnaire(index, e)}>
                <option value="null" />
                <option value="SO">Sans objet</option>
                {options}
              </select>
            </td>
          </tr>
        );
      });
      const listeRestante = this.props.datacubeSelected.components.map((component, index) => {
        if (this.props.choixGestionnaire.includes(component.iri)) {
          return null;
        } else if (component.modalites && component.modalites.length > 0) {
          const options = component.modalites.map(modalite => <option value={modalite.code}>{modalite.libelleFr}</option>);
          return (
            <li key={index}>
              {component.libelleFr}{" "}
              <select onChange={e => this.onChangeParDefaut(index, e)}>
                <option value="null" />
                {options}
              </select>
            </li>
          );
        } else {
          let input = null;
          if (this.props.choixParDefaut[index]) {
            input = <input className="input-inline" value={this.props.choixParDefaut[index]} onChange={e => this.onChangeParDefaut(index, e)} type="text" />;
          } else {
            input = <input className="input-inline" value="" onChange={e => this.onChangeParDefaut(index, e)} type="text" />;
          }
          return (
            <li key={index}>
              {component.libelleFr} {input}
            </li>
          );
        }
      });
      tableau = (
        <div className="row">
          <div className="large-7 columns">
            <table>
              <thead>
                <tr>
                  <th>Fichier</th>
                  <th>Proposition</th>
                  <th>Choix gestionnaire</th>
                </tr>
              </thead>
              <tbody>{body}</tbody>
            </table>
          </div>
          <div className="large-5 columns attributs-manquants">
            <ul>{listeRestante}</ul>
          </div>
        </div>
      );
    }
    return (
      <div className="large-12 large-centered columns">
        <fieldset>
          <legend>Chargement</legend>
          <div className="row">
            <div className="large-4 columns">
              <label>
                Choisir un cube <span className="required">*</span>{" "}
              </label>
              <select onChange={this.onChangeDatacube}>
                <option />
                {optionsDatacubes}
              </select>
            </div>
            <div className="large-4 columns">
              <label>
                Fichier à charger <span className="required">*</span>{" "}
              </label>
              <input onChange={this.onChangeFile} type="file" />
            </div>
            <div className="large-4 columns" />
          </div>
          {tableau}
          {boutonValidation ? (
            <div className="row">
              <div className="large-3 columns">
                <input type="submit" value="Valider" onClick={this.onClickValider} />
              </div>
              {this.props.urlTelechargement ? (
                <div className="large-3 columns">
                  <input type="submit" value="Télécharger Turtle" onClick={this.onClickTelecharger} />
                </div>
              ) : null}
            </div>
          ) : null}
        </fieldset>
      </div>
    );
  }
}

const mapStateToProps = ({ chargementReducer, visualisationReducer }) => {
  return {
    infosFichier: chargementReducer.infosFichier,
    colonnes: chargementReducer.colonnes,
    datacubeSelected: chargementReducer.datacubeSelected,
    datacubes: visualisationReducer.datacubes,
    lienFichier: chargementReducer.lienFichier,
    choixGestionnaire: chargementReducer.choixGestionnaire,
    choixParDefaut: chargementReducer.choixParDefaut
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
    setInfosFichier: infosFichier => {
      dispatch(setInfosFichier(infosFichier));
    },
    setColonnes: colonnes => {
      dispatch(setColonnes(colonnes));
    },
    fetchDatacubes: () => {
      dispatch(fetchDatacubes());
    },
    selectionDatacube: (datacube, setDatacube) => {
      dispatch(selectionDatacube(datacube, setDatacube));
    },
    changeChoixGestionnaire: (choix, index) => {
      dispatch(changeChoixGestionnaire(choix, index));
    },
    changeChoixParDefaut: (choix, index) => {
      dispatch(changeChoixParDefaut(choix, index));
    }
  };
};

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ChargementFichier);
