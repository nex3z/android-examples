import 'package:flutter/material.dart';
import 'package:web_socket_channel/io.dart';

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
  final _addressController = TextEditingController(text: 'ws://echo.websocket.org');
  final _messageController = TextEditingController(text: 'Hello');
  IOWebSocketChannel _channel = null;

  void _connect() {
    var address = _addressController.text;
    print("address = $address");
    setState(() {
      _channel = IOWebSocketChannel.connect(address);
    });
  }

  void _send() {
    if (_channel == null) {
      return;
    }
    var message = _messageController.text;
    print("message = $message");
    _channel.sink.add(message);
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
            Row(
              children: <Widget>[
                Flexible(
                  flex: 1,
                  child: TextField(
                    style: Theme.of(context).textTheme.body1,
                    decoration: InputDecoration(
                      labelText: 'Address',
                    ),
                    controller: _addressController,
                  ),
                ),
                Flexible(
                  flex: 0,
                  child: RaisedButton(
                    child: Text('Connecct'),
                    onPressed: _connect,
                  ),
                )
              ],
            ),
            Row(
              children: <Widget>[
                Flexible(
                  child: TextField(
                    style: Theme.of(context).textTheme.body1,
                    decoration: InputDecoration(
                      labelText: 'Message',
                    ),
                    controller: _messageController,
                  ),
                ),
                Flexible(
                  flex: 0,
                  child: RaisedButton(
                    child: Text('Send'),
                    onPressed: _send,
                  ),
                )
              ],
            ),
            _channel != null ? Padding(
              padding: EdgeInsets.only(top: 16.0),
              child:  StreamBuilder(
                stream: _channel.stream,
                builder: (context, snapshot) {
                  return Padding(
                    padding: const EdgeInsets.symmetric(vertical: 24.0),
                    child: Text(snapshot.hasData ? '${snapshot.data}' : ''),
                  );
                },
              )
            ) : Container(),
          ],
        ),
      ),
    );
  }
}
