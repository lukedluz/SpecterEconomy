package com.lucas.spectergold.hook;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.OfflinePlayer;

import com.lucas.spectergold.Main;
import com.lucas.spectergold.utils.Formater;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.economy.EconomyResponse.ResponseType;

public class VaultHandler implements Economy{
	
	@Override
	public EconomyResponse bankBalance(String arg0) {
		return new EconomyResponse(0.0, 0.0, ResponseType.NOT_IMPLEMENTED, "§cEste plugin nao possui suporte para este tipo de acao.");
	}

	@Override
	public EconomyResponse bankDeposit(String arg0, double arg1) {
		return new EconomyResponse(0.0, 0.0, ResponseType.NOT_IMPLEMENTED, "§cEste plugin nao possui suporte para este tipo de acao.");
	}

	@Override
	public EconomyResponse bankHas(String arg0, double arg1) {
		return new EconomyResponse(0.0, 0.0, ResponseType.NOT_IMPLEMENTED, "§cEste plugin nao possui suporte para este tipo de acao.");
	}

	@Override
	public EconomyResponse bankWithdraw(String arg0, double arg1) {
		return new EconomyResponse(0.0, 0.0, ResponseType.NOT_IMPLEMENTED, "§cEste plugin nao possui suporte para este tipo de acao.");
	}

	@Override
	public EconomyResponse createBank(String arg0, String arg1) {
		return new EconomyResponse(0.0, 0.0, ResponseType.NOT_IMPLEMENTED, "§cEste plugin nao possui suporte para este tipo de acao.");
	}

	@Override
	public EconomyResponse createBank(String arg0, OfflinePlayer arg1) {
		return new EconomyResponse(0.0, 0.0, ResponseType.NOT_IMPLEMENTED, "§cEste plugin nao possui suporte para este tipo de acao.");
	}

	@Override
	public boolean createPlayerAccount(String arg0) {
		boolean sucess = false;
		try {
			Main.getGoldCore().createAccount(arg0);
			Main.getGoldCore().getCached(arg0).save(true);
			sucess = true;
		} catch (SQLException e) {
	//		e.printStackTrace();
		}
		return sucess;
	}

	@Override
	public boolean createPlayerAccount(OfflinePlayer arg0) {
		return createPlayerAccount(arg0.getName());
	}

	@Override
	public boolean createPlayerAccount(String player, String arg1) {
		return createPlayerAccount(player);
	}

	@Override
	public boolean createPlayerAccount(OfflinePlayer arg0, String arg1) {
		return createPlayerAccount(arg0);
	}

	@Override
	public String currencyNamePlural() {
		return "Gold";
	}

	@Override
	public String currencyNameSingular() {
		return "Gold";
	}

	@Override
	public EconomyResponse deleteBank(String arg0) {
		return new EconomyResponse(0.0, 0.0, ResponseType.NOT_IMPLEMENTED, "§cEste plugin nao possui suporte para este tipo de acao.");
	}

	@Override
	public EconomyResponse depositPlayer(String player, double valor) {
		if (valor > 0){
			Main.getGoldCore().add(player, valor);
			return new EconomyResponse(0, Main.getGoldCore().getSaldo(player), ResponseType.SUCCESS, "");
		} else {
			return new EconomyResponse(0, 0, ResponseType.FAILURE, "§cValor negativo");
		}
	}

	@Override
	public EconomyResponse depositPlayer(OfflinePlayer player, double valor) {
		return depositPlayer(player.getName(), valor);
	}

	@Override
	public EconomyResponse depositPlayer(String arg0, String arg1, double arg2) {
		return depositPlayer(arg0, arg2);
	}

	@Override
	public EconomyResponse depositPlayer(OfflinePlayer player, String arg1,
			double valor) {
		return depositPlayer(player.getName(), valor);
	}

	@Override
	public String format(double valor) {
		return Formater.format(valor);
	}

	@Override
	public int fractionalDigits() {
		return -1;
	}

	@Override
	public double getBalance(String player) {
		return Main.getGoldCore().getSaldo(player);
	}

	@Override
	public double getBalance(OfflinePlayer player) {
		return getBalance(player.getName());
	}

	@Override
	public double getBalance(String player, String arg1) {
		return getBalance(player);
	}

	@Override
	public double getBalance(OfflinePlayer player, String arg1) {
		return getBalance(player.getName());
	}

	@Override
	public List<String> getBanks() {
		
		return new ArrayList<String>();
		
	}

	@Override
	public String getName() {
		return "SpecterEconomy";
	}

	@Override
	public boolean has(String player, double valor) {
		
		double saldo = Main.getGoldCore().getSaldo(player);
	//	if (p != null){
			if (valor >= 0){
				return saldo >= valor;
			} else {
				return false;
			}
	//	} else {
	//		return false;
	//	}
	}

	@Override
	public boolean has(OfflinePlayer player, double valor) {
		return has(player.getName(), valor);
	}

	@Override
	public boolean has(String player, String arg1, double valor) {
		return has(player, valor);
	}

	@Override
	public boolean has(OfflinePlayer player, String arg1, double valor) {
		return has(player.getName(), valor);
	}

	@Override
	public boolean hasAccount(String player) {
		return Main.getGoldCore().getCached(player) != null;
	}

	@Override
	public boolean hasAccount(OfflinePlayer arg0) {
		return hasAccount(arg0.getName());
	}

	@Override
	public boolean hasAccount(String arg0, String arg1) {
		return hasAccount(arg0);
	}

	@Override
	public boolean hasAccount(OfflinePlayer arg0, String arg1) {
		return hasAccount(arg0.getName());
	}

	@Override
	public boolean hasBankSupport() {
		return false;
	}

	@Override
	public EconomyResponse isBankMember(String arg0, String arg1) {
		return new EconomyResponse(0.0, 0.0, ResponseType.NOT_IMPLEMENTED, "§cEste plugin nao possui suporte para este tipo de acao.");
	}

	@Override
	public EconomyResponse isBankMember(String arg0, OfflinePlayer arg1) {
		return new EconomyResponse(0.0, 0.0, ResponseType.NOT_IMPLEMENTED, "§cEste plugin nao possui suporte para este tipo de acao.");
	}

	@Override
	public EconomyResponse isBankOwner(String arg0, String arg1) {
		return new EconomyResponse(0.0, 0.0, ResponseType.NOT_IMPLEMENTED, "§cEste plugin nao possui suporte para este tipo de acao.");
	}

	@Override
	public EconomyResponse isBankOwner(String arg0, OfflinePlayer arg1) {
		return new EconomyResponse(0.0, 0.0, ResponseType.NOT_IMPLEMENTED, "§cEste plugin nao possui suporte para este tipo de acao.");
	}

	@Override
	public boolean isEnabled() {
		return Main.getInstance().isEnabled();
	}

	@Override
	public EconomyResponse withdrawPlayer(String player, double valor) {

		if (valor < 0){
			return new EconomyResponse(0, 0, ResponseType.FAILURE, "Nagative value");
		}
		Main.getGoldCore().remove(player, valor);
		return new EconomyResponse(0, Main.getGoldCore().getSaldo(player), ResponseType.SUCCESS, "");
	}

	@Override
	public EconomyResponse withdrawPlayer(OfflinePlayer player, double valor) {
		return withdrawPlayer(player.getName(), valor);
	}

	@Override
	public EconomyResponse withdrawPlayer(String player, String arg1, double valor) {
		return withdrawPlayer(player, valor);
	}

	@Override
	public EconomyResponse withdrawPlayer(OfflinePlayer player, String arg1,
			double valor) {
		return withdrawPlayer(player.getName(), valor);
	}

}
