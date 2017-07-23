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
  View
} from 'react-native';

import { NativeModules } from 'react-native';
const UsageStats = NativeModules.UsageStats;

export default class SampleApp extends Component {
  constructor(props) {
    super(props);
    this.getStats = this.getStats.bind(this);
    this.parseStats = this.parseStats.bind(this);

    this.state = { stats: [] };
    this.getStats();
  }

  getStats() {
    UsageStats.getStats(message => {
      const stats = this.parseStats(message);
      this.setState({ stats });
    });
  }

  parseStats(unparsed) {
    const appsAndTimes = unparsed.split(';');
    const times = appsAndTimes[1].split(',')
      .map(val => parseInt(val));
    const apps = appsAndTimes[0].split(',')
      .map(name => name.split('.').pop());

    const stats = [];

    for (let i = 0; i < apps.length; i++) {
      stats.push({
        name: apps[i],
        time: times[i]
      });
    }

    return stats;
  }

  render() {
    // UsageStats.testToast(UsageStats.SHORT);
    // console.log(this.state.stats);
    return (
      <View style={styles.container}>
        <Text style={styles.welcome}>
          UsageStats React Native Module Sample App
        </Text>
        <Text style={styles.instructions}>
          Instructions coming soon...
        </Text>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  welcome: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
  },
  instructions: {
    textAlign: 'center',
    color: '#333333',
    marginBottom: 5,
  },
});

AppRegistry.registerComponent('SampleApp', () => SampleApp);
