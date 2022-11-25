//// Asynchronous Client Socket Example
//// http://msdn.microsoft.com/en-us/library/bew39x2a.aspx

//using System;
//using System.Net;
//using System.Net.Sockets;
//using System.Threading;
//using System.Text;


//// State object for receiving data from remote device.
//public class StateObject
//{
//    // Client socket.
//    public Socket workSocket = null;
//    // Size of receive buffer.
//    public const int BufferSize = 256;
//    // Receive buffer.
//    public byte[] buffer = new byte[BufferSize];
//    // Received data string.
//    public StringBuilder sb = new StringBuilder();
//}


//public class AsynchronousClient
//{
//    // The port number for the remote device.
//    private const int port = 80;

//    // ManualResetEvent instances signal completion.
//    private ManualResetEvent connectDone =
//        new ManualResetEvent(false);
//    private ManualResetEvent sendDone =
//        new ManualResetEvent(false);
//    private ManualResetEvent receiveDone =
//        new ManualResetEvent(false);

//    // The response from the remote device.
//    public String response = String.Empty;

//    private string host;
//    private string page;

//    private Socket client;


//    public AsynchronousClient(string host, string page)
//    {
//        this.host = host;
//        this.page = page;

//    }

//    public AsynchronousClient(string url)
//    {
//        splitUrl(url);

//    }

//    private void splitUrl(string url)
//    {
//        var tokens = url.Split('/', 2);
//        Console.WriteLine(tokens[0] + " | " + tokens[1]);
//        this.host = tokens[0];
//        this.page = "/" + tokens[1];
//    }

//    public string generateGet()
//    {
//        StringBuilder sb = new StringBuilder();
//        sb.Append("GET ")
//            .Append(page)
//            .Append(" HTTP/1.1\r\nHOST: ")
//            .Append(host)
//            .Append("\r\n\r\n");
//        return sb.ToString();
//    }

//    public Task connect()
//    {
//        Task.Factory.StartNew(() => startConnection());
//        return Task.FromResult(connectDone.WaitOne());
//    }

//    public void startConnection()
//    {

//        IPHostEntry ipHostInfo = Dns.GetHostEntry(host);
//        Console.WriteLine(ipHostInfo);
//        IPAddress ipAddress = ipHostInfo.AddressList[0];
//        IPEndPoint remoteEP = new IPEndPoint(ipAddress, port);

//        // Create a TCP/IP socket.
//        client = new Socket(AddressFamily.InterNetwork,
//            SocketType.Stream, ProtocolType.Tcp);

//        // Connect to the remote endpoint.
//        client.BeginConnect(remoteEP,
//            new AsyncCallback(ConnectCallback), client);
//    }

//    private void ConnectCallback(IAsyncResult ar)
//    {
//        try
//        {

//            // Complete the connection.
//            client.EndConnect(ar);

//            Console.WriteLine("Socket connected to {0}",
//                client.RemoteEndPoint.ToString());

//            // Signal that the connection has been made.
//            connectDone.Set();
//        }
//        catch (Exception e)
//        {
//            Console.WriteLine(e.ToString());
//        }
//    }

//    public Task receive()
//    {
//        Task.Factory.StartNew(() => Receive());
//        return Task.FromResult(receiveDone.WaitOne());
//    }

//    private void Receive()
//    {
//        try
//        {
//            // Create the state object.
//            StateObject state = new StateObject();
//            state.workSocket = client;

//            // Begin receiving the data from the remote device.
//            client.BeginReceive(state.buffer, 0, StateObject.BufferSize, 0,
//                new AsyncCallback(ReceiveCallback), state);
//        }
//        catch (Exception e)
//        {
//            Console.WriteLine(e.ToString());
//        }
//    }

//    private void ReceiveCallback(IAsyncResult ar)
//    {
//        try
//        {
//            // Retrieve the state object and the client socket 
//            // from the asynchronous state object.
//            StateObject state = (StateObject)ar.AsyncState;

//            // Read data from the remote device.
//            int bytesRead = client.EndReceive(ar);

//            if (bytesRead > 0)
//            {
//                // There might be more data, so store the data received so far.
//                state.sb.Append(Encoding.ASCII.GetString(state.buffer, 0, bytesRead));

//                // Get the rest of the data.
//                client.BeginReceive(state.buffer, 0, StateObject.BufferSize, 0,
//                    new AsyncCallback(ReceiveCallback), state);
//            }
//            else
//            {
//                // All the data has arrived; put it in response.
//                if (state.sb.Length > 1)
//                {
//                    response = state.sb.ToString();
//                }
//                // Signal that all bytes have been received.
//                receiveDone.Set();


//                // Release the socket.
//                client.Shutdown(SocketShutdown.Both);
//                client.Close();
//            }
//        }
//        catch (Exception e)
//        {
//            Console.WriteLine(e.ToString());
//        }
//    }

//    public Task send(String data)
//    {
//        Task.Factory.StartNew(() => Send(data));
//        return Task.FromResult(sendDone.WaitOne());
//    }

//    private void Send(String data)
//    {
//        // Convert the string data to byte data using ASCII encoding.
//        byte[] byteData = Encoding.ASCII.GetBytes(data);

//        // Begin sending the data to the remote device.
//        client.BeginSend(byteData, 0, byteData.Length, 0,
//            new AsyncCallback(SendCallback), client);
//    }

//    private void SendCallback(IAsyncResult ar)
//    {
//        try
//        {
//            // Retrieve the socket from the state object.

//            // Complete sending the data to the remote device.
//            int bytesSent = client.EndSend(ar);
//            Console.WriteLine("Sent {0} bytes to server.", bytesSent);

//            // Signal that all bytes have been sent.
//            sendDone.Set();
//        }
//        catch (Exception e)
//        {
//            Console.WriteLine(e.ToString());
//        }
//    }
//}

//public static class Program
//{
//    public static int Main(String[] args)
//    {
//        string[] addresses = new string[] { "www.cs.ubbcluj.ro/~rlupsa/sci/",
//            "www.cs.ubbcluj.ro/~rlupsa/edu/pdp/lab-4-futures-continuations.html",
//            "www.cs.ubbcluj.ro/~rlupsa/edu/pdp/index.html" };
//        foreach (var url in addresses)
//        {
//            AsynchronousClient client = new AsynchronousClient(url);
//            client.connect();
//            client.send(client.generateGet());
//            client.receive();
//            Console.WriteLine(client.response); 
            
//        }
//        return 0;
//    }
//}