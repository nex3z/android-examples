import 'package:flutter/material.dart';
import 'package:web_socket_channel/io.dart';

import 'ConnectWidget.dart';
import 'ReceiveMessageWidget.dart';
import 'SendMessageWidget.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: MyHomePage(),
    );
  }
}

class MyHomePage extends StatefulWidget {
  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  IOWebSocketChannel _channel;

  void _onConnected(IOWebSocketChannel channel) {
    setState(() {
      _channel = channel;
    });
  }

  void _onDisconnected() {
    setState(() {
      _channel = null;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Websocket Client'),
      ),
      body: Container(
        padding: EdgeInsets.all(16.0),
        child: Column(
          children: <Widget>[
            ConnectWidget(
              onConnected: _onConnected,
              onDisconnected: _onDisconnected,
            ),
            SendMessageWidget(
              channel: _channel,
            ),
            Padding(
              padding: EdgeInsets.only(top: 32.0),
              child: ReceiveMessageWidget(
                channel: _channel,
              ),
            ),
          ],
        )
      ),
    );
  }
}
