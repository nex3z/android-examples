/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import React, { Component } from 'react';
import {
  AppRegistry,
  StyleSheet,
  Text,
  TextInput,
  View
} from 'react-native';

export default class ReactNativePlayground extends Component {
  constructor(props) {
    super(props);
    this.state = {text: ''};
  }

  render() {
    return (
      <View style={styles.container}>
        <TextInput
          style={{fontSize: 24}}
          placeholder="Input your name"
          onChangeText={(text) => this.setState({text})}
        />
        <Text style={{fontSize: 48}}>
          {"Hello " + this.state.text}
        </Text>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    padding: 10,
  },
});

AppRegistry.registerComponent('ReactNativePlayground', () => ReactNativePlayground);
