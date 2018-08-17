import React from "react";
import { connect } from "react-redux";

class Waiting extends React.Component {
  constructor(props) {
    super(props);
  }

  render() {
    const { context, waiting } = this.props;
    if (waiting > 0) {
      return (
        <div className="wait-on">
          <img alt="Veuillez-patienter..." src={context + "/static/img/preloader.gif"} />
        </div>
      );
    } else {
      return null;
    }
  }
}

const mapStateToProps = ({ generalReducer }) => {
  return { waiting: generalReducer.waiting, context: generalReducer.context };
};

const mapDispatchToProps = dispatch => {
  return {};
};

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Waiting);
