///**************************************************************************
// *
// *  Copyright 2014, Roger Brown
// *
// *  This file is part of Roger Brown's Toolkit.
// *
// *  This program is free software: you can redistribute it and/or modify it
// *  under the terms of the GNU Lesser General Public License as published by the
// *  Free Software Foundation, either version 3 of the License, or (at your
// *  option) any later version.
// *
// *  This program is distributed in the hope that it will be useful, but WITHOUT
// *  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// *  FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
// *  more details.
// *
// *  You should have received a copy of the GNU Lesser General Public License
// *  along with this program.  If not, see <http://www.gnu.org/licenses/>
// *
// */
//
///*
// * $Id: CardHandler.java 2 2015-10-29 19:59:34Z rhubarb-geek-nz $
// */
//
//package com.example.cameronshotter.snapperapp.emvtoolt;
//
//import java.io.IOException;
//import java.util.Iterator;
//import java.util.List;
//import java.util.logging.Level;
//import java.util.logging.Logger;
////import javax.smartcardio.Card;
////import javax.smartcardio.CardChannel;
////import javax.smartcardio.CardException;
////import javax.smartcardio.CardTerminal;
////import javax.smartcardio.CardTerminals;
////import javax.smartcardio.CommandAPDU;
////import javax.smartcardio.TerminalFactory;
//import com.example.cameronshotter.snapperapp.emvtools.EMVReader;
//import com.example.cameronshotter.snapperapp.emvtools.Hex;
//
//public class CardHandler implements EMVReader.CardReader
//{
//    public static boolean doTrace=false;
//    private final CardChannel channel;
//    private final Card card;
//    private final byte[] adfInfo;
//    private final String name;
//
//    public static void t0fix()
//    {
//          System.setProperty("sun.security.smartcardio.t0GetResponse", "false");
//    }
//
//    public static CardHandler findCard(byte[] selectApp) throws CardException
//    {
//        int nameIt=0;
//        System.out.println("Default Terminal Type "+TerminalFactory.getDefaultType());
//        TerminalFactory tf = TerminalFactory.getDefault();
//        System.out.println("Default Terminal Factory"+tf);
//
//        CardTerminals ct = tf.terminals();
//
//        List<CardTerminal> list = ct.list();
//        Iterator<CardTerminal> it = list.iterator();
//
//        while (it.hasNext())
//        {
//            CardTerminal t = it.next();
//
//            System.out.print("CardTerminal "+t);
//
//            if (t.waitForCardPresent(5000))
//            {
//                Card c = t.connect("*");
//
//                System.out.print("Card "+c);
//
//                CardChannel ch = c.getBasicChannel();
//
//                System.out.println("CardChannel "+ch);
//
//                if (ch != null)
//                {
//                    byte[] resp = transceive(ch, selectApp);
//                    if (resp != null)
//                    {
//                        if ((resp.length >= 2)
//                                && (resp[resp.length - 2] == (byte) 0x90)
//                                && (resp[resp.length - 1] == (byte) 0x00))
//                        {
//                            System.err.println("name "+t.getName());
//
//                            return new CardHandler(t.getName(),c, ch, resp);
//                        }
//                    }
//            //        c.disconnect(false);
//                }
//            }
//            else
//            {
//                System.out.println("No card present");
//            }
//        }
//
//        return null;
//    }
//
//    private CardHandler(String n,Card c, CardChannel ch, byte[] rb)
//    {
//        card = c;
//        channel = ch;
//        adfInfo = rb;
//        name=n;
//    }
//
//    private static byte[] transceive(CardChannel ch, byte[] data) throws CardException
//    {
//        if (doTrace)
//        {
//            System.err.println(">> "+Hex.encode(data,0,data.length));
//        }
//
//        byte[] b = ch.transmit(new CommandAPDU(data)).getBytes();
//
//        if ((b.length == 2) && (b[0] == 0x61))
//        {
//            byte[] getData = new byte[]
//            {
//                0x00, (byte) 0xC0, 0x00, 0x00, b[1]
//            };
//
//            b = ch.transmit(new CommandAPDU(getData)).getBytes();
//        }
//
//        if (doTrace)
//        {
//            System.err.println("<< "+Hex.encode(b,0,b.length));
//        }
//
//        return b;
//    }
//
//    public byte[] transceive(byte[] packet) throws IOException
//    {
//        byte b[] = null;
//
//        try
//        {
//            b = transceive(channel, packet);
//
//        }
//        catch (CardException ex)
//        {
//            Logger.getLogger(CardHandler.class.getName()).log(Level.SEVERE, null, ex);
//            throw new IOException(ex);
//        }
//
//        return b;
//    }
//
//    public byte[] getADF()
//    {
//        return adfInfo;
//    }
//}
