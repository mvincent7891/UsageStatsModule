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
  View,
  TextInput
} from 'react-native';

import { NativeModules } from 'react-native';
const UsageStats = NativeModules.UsageStats;

import _ from 'lodash';

export default class SampleApp extends Component {
  constructor(props) {
    super(props);
    this.getStats = _.debounce(this.getStats.bind(this), 1000);
    this.parseStats = this.parseStats.bind(this);
    this.getStatsComponents = this.getStatsComponents.bind(this);
    this.updateDuration = this.updateDuration.bind(this);

    this.state = {
      stats: [],
      durationInDays: 7
    };
    this.getStats();
  }

  updateDuration(val) {
    let newVal = val;
    if (!(parseInt(val) >= 0)) {
      newVal = 0;
    }
    this.setState({
      durationInDays: parseInt(newVal)
    }, this.getStats);
  }

  getStats() {
    const { durationInDays } = this.state;
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
    return _.sortBy(stats, ['time'])
      .reverse()
      .slice(0,5).map((stat, idx) => (
      <Text
        style={styles.stat}
        key={`app-${idx}`}>
        {`${stat.name}: ${stat.time} ms`}
      </Text>
    ));
  }

  render() {
    const { durationInDays } = this.state;
    return (
      <View style={styles.container}>
        <View>
          <Text style={styles.welcome}>
            UsageStats React Native Module Sample App
          </Text>
          <Text style={styles.instructions}>
            Duration in foreground (5 most used apps):
          </Text>
        </View>
        <View style={styles.statsContainer}>
          { this.getStatsComponents() }
        </View>
        <View style={styles.inputContainer}>
          <Text>
            {`Showing the past ${durationInDays} day(s):`}
          </Text>
          <TextInput
            style={styles.input}
            onChangeText={this.updateDuration}
            value={durationInDays ? durationInDays.toString() : ''}
            />
        </View>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  input: {
    height: 40,
    width: 60,
    borderColor: 'gray',
    textAlign: 'right',
    borderWidth: 1,
    marginTop: 20,
    marginBottom: 50
  },
  inputContainer: {
    flex: 1,
    borderColor: 'gray',
    borderTopWidth: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#fff',
    width: '100%',
    marginTop: 100
  },
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
