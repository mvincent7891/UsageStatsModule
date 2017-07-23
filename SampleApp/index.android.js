/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

 /*-------------------------------------------------\
 |                                                  |
 | UsageStatsModule                                 |
 | Sample app extended by Michael Parlato           |
 | Source code:                                     |
 | https://github.com/mvincent7891/UsageStatsModule |
 |                                                  |
 | Credit to Cole Murray:                           |
 | https://github.com/ColeMurray/UsageStatsSample   |
 |                                                  |
 \-------------------------------------------------*/

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
    this.getStatsComponents = this.getStatsComponents.bind(this);

    this.state = { stats: [] };
    this.getStats(7);
  }

  getStats(durationInDays) {
    UsageStats.getStats(durationInDays, message => {
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

  getStatsComponents() {
    const { stats } = this.state;
    return stats.map((stat, idx) => (
      <Text
        style={styles.stat}
        key={`app-${idx}`}>
        {`${stat.name}: ${stat.time}`}
      </Text>
    ));
  }

  render() {

    /*----------------------------------------------
    | Test that everything is working alright:     |
    | UsageStats.testToast(UsageStats.SHORT);      |
    |                                              |
    |                                              |
    | console.log(this.state.stats);               |
    -----------------------------------------------*/
    return (
      <View style={styles.container}>
        <View>
          <Text style={styles.welcome}>
            UsageStats React Native Module Sample App
          </Text>
          <Text style={styles.instructions}>
            Duration in foreground statistics for all applications:
          </Text>
        </View>
        <View style={styles.statsContainer}>
          { this.getStatsComponents() }
        </View>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    marginTop: 100
  },
  statsContainer: {
    flex: 1,
    justifyContent: 'flex-start',
    alignItems: 'flex-start'
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
  stat: {
    textAlign: 'left',
    color: '#777777',
    marginTop: 5,
  }
});

AppRegistry.registerComponent('SampleApp', () => SampleApp);
