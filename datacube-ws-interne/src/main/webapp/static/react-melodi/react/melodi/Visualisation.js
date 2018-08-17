import React from "react";
import { connect } from "react-redux";
import { FilAriane } from "../commun/FilAriane";
import { initGet } from "../_utils";
import { WS_CONTEXT_PATH } from "../_properties";
import { waitingTrue, waitingFalse } from "../commun/GeneralReducer";
import { fetchDatacubes, selectionDatacube, setSeries, changeModaliteChoisie, setDatacube } from "./VisualisationReducer";

class Visualisation extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      donnees: []
    };
    this.handleClickDatacube = this.handleClickDatacube.bind(this);
    this.handleChangeModalite = this.handleChangeModalite.bind(this);
  }

  componentDidMount() {
    this.props.fetchDatacubes();
  }

  handleClickDatacube(datacube, e) {
    this.props.selectionDatacube(datacube, setDatacube);
  }

  handleChangeModalite(dimension, e) {
    this.props.changeModaliteChoisie(dimension, e.target.value !== "null" ? e.target.value : null);
  }

  onChangeTimePeriod = e => {
    this.props.changeModaliteChoisie({ iri: "timePeriod", code: "timePeriodRef" }, e.target.value !== "null" ? e.target.value : null);
  };

  componentWillReceiveProps(newProps) {
    if (this.props.modalitesChoisies !== newProps.modalitesChoisies || this.props.datacubeSelected != newProps.datacubeSelected) {
      this.props.waitingTrue();
      let params = newProps.modalitesChoisies.map(item => item.dimension.code + "=" + item.modalite).join("&");
      if (params) {
        params = "?" + params;
      }
      fetch(WS_CONTEXT_PATH + "/datacube/" + newProps.datacubeSelected.code + "/series/valeurs" + params, initGet())
        .then(response => response.json())
        .then(json => {
          this.props.waitingFalse();
          this.props.setSeries(json);
        })
        .catch(error => {
          console.log(error);
        });
    }
  }

  render() {
    const datacubes = this.props.datacubes.map((datacube, index) => (
      <li key={index} onClick={e => this.handleClickDatacube(datacube, e)} className="li-dataflow">
        {datacube.libelleFr}
      </li>
    ));

    let parametres = null;
    if (this.props.datacubeSelected) {
      parametres = this.props.datacubeSelected.dimensions.map((dimension, index) => {
        const modaliteChoisi = this.props.modalitesChoisies.find(modalite => modalite.dimension === dimension);
        const select = dimension.modalites.map((modalite, index) => {
          if (modaliteChoisi && modaliteChoisi.modalite === modalite.code) {
            return (
              <option selected="selected" key={index} value={modalite.code}>
                {modalite.code} - {modalite.libelleFr}
              </option>
            );
          } else {
            return (
              <option key={index} value={modalite.code}>
                {modalite.code} - {modalite.libelleFr}
              </option>
            );
          }
        });
        return (
          <li key={index}>
            {dimension.libelleFr} :{" "}
            <select onChange={e => this.handleChangeModalite(dimension, e)}>
              <option value="null">Aucun</option>
              {select}
            </select>
          </li>
        );
      });

      const timePeriodChoisie = this.props.modalitesChoisies.find(modalite => modalite.dimension.iri === "timePeriod");
      const timePeriodValue = timePeriodChoisie ? timePeriodChoisie.modalite : "";
      parametres.splice(
        0,
        0,
        <li>
          Time period : <input className="input-inline" value={timePeriodValue} onChange={this.onChangeTimePeriod} type="text" />
        </li>
      );
    }
    let table = null;
    if (this.props.series.length > 0) {
      const isSlice = this.props.series[0].iri ? true : false;
      const headerTable = this.props.series.map((serie, index) => <td key={index}>{serie.code}</td>);
      const annees = this.props.series[0].observations.map(valeur => valeur.timePeriod);
      const bodyTable = annees.map((annee, index) => {
        const cellule = this.props.series.map((serie, index2) => <td key={index2}>{serie.observations[index].valeur}</td>);
        return (
          <tr key={index}>
            {isSlice ? <td>{annee}</td> : null}
            {cellule}
          </tr>
        );
      });
      table = (
        <table className="table-donnees">
          <thead>
            <tr>
              {isSlice ? <td>Année</td> : null}
              {headerTable}
            </tr>
          </thead>
          <tbody>{bodyTable}</tbody>
        </table>
      );
    }
    const filAriane = [
      {
        href: "/los/react/melodi",
        libelle: "MELODI"
      },
      { libelle: "Visualiser les données" }
    ];
    return (
      <div>
        <FilAriane filAriane={filAriane} titre="Visualiser les données" />
        <div className="large-12 large-centered columns bid">
          <div className="visualisation-tableau">
            <div className="dataflows">
              <h3>Datacubes</h3>
              <ul>{datacubes}</ul>
            </div>
            <div className="parametres">
              <h3>Paramètres</h3>
              <ul>{parametres}</ul>
            </div>
            <div className="donnees">
              <h3>Données</h3>
              {table}
            </div>
          </div>
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ visualisationReducer }) => {
  return {
    datacubes: visualisationReducer.datacubes,
    datacubeSelected: visualisationReducer.datacubeSelected,
    series: visualisationReducer.series,
    modalitesChoisies: visualisationReducer.modalitesChoisies
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
    fetchDatacubes: () => {
      dispatch(fetchDatacubes());
    },
    selectionDatacube: (datacube, setDatacube) => {
      dispatch(selectionDatacube(datacube, setDatacube));
    },
    setSeries: series => {
      dispatch(setSeries(series));
    },
    changeModaliteChoisie: (dimension, modalite) => {
      dispatch(changeModaliteChoisie(dimension, modalite));
    }
  };
};

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Visualisation);
