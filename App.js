import axios from 'axios';
import React from 'react';
import {
  SafeAreaView,
  StatusBar,
  StyleSheet,
  useColorScheme,
  TextInput,
  View,
  NativeModules,
  Button,
} from 'react-native';

import {Colors} from 'react-native/Libraries/NewAppScreen';

/* $FlowFixMe[missing-local-annot] The type annotation(s) required by Flow's
 * LTI update could not be added via codemod */
const App = () => {
  const isDarkMode = useColorScheme() === 'dark';

  const backgroundStyle = {
    backgroundColor: isDarkMode ? Colors.darker : Colors.lighter,
  };

  const [number, onChangeNumber] = React.useState(null);

  const onButtonPress = () => {
    const headers = {
      'x-client-id': 'bbmclp-clidecimal-gtw4rt67yuop',
      'x-app-identifier': 'MPP',
      'x-api-key': 'c7d334z99-99af-0e69-c961-1994s2sd786y',
      'x-organization-id': 'CONSUMER002',
      'Content-Type': 'application/json',
    };
    axios
      .post(
        'https://mpp-client-uat.moneyone.in/mpp/consent',
        {
          productID: '4',
          partyIdentifierType: 'MOBILE',
          partyIdentifierValue: number,
          accountID: '123',
        },
        {headers: headers},
      )
      .then(response => {
        NativeModules.MPP.initialiseMPP({
          url: 'https://mpp-api-uat.moneyone.in',
          mobileNumber: number,
          consentHandle: response.data.data.consent_handle,
          clientId: '685fae51-4eff-4a52-847d-1ec9d82133b9',
          appIdentifier: 'MPP',
          apiKey: 'aH2nO3jN6iH6gD7nV2nX3eD8jD7bG5vM0rA6mY3kB2wT0rN4gL',
          organisationId: 'CONSUMER002',
          ssoTimeStamp: '',
        });
      })
      .catch();
  };

  return (
    <SafeAreaView style={{backgroundStyle}}>
      <StatusBar
        barStyle={isDarkMode ? 'light-content' : 'dark-content'}
        backgroundColor={backgroundStyle.backgroundColor}
      />
      <View>
        <TextInput
          style={styles.input}
          onChangeText={onChangeNumber}
          value={number}
          placeholder="Enter Number"
          keyboardType="numeric"
          maxLength={10}
        />
        <Button title="Initialise" onPress={onButtonPress} />
      </View>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({});

export default App;
